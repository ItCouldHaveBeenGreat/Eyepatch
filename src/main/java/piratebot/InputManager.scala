package main.java.piratebot

import scala.collection.mutable

class InputManager(game: Game) {
    private val inputRequests : mutable.HashMap[Int, InputRequest] = mutable.HashMap[Int, InputRequest]()

    def getInputRequest(playerId : Int): InputRequest = {
        if (inputRequests.contains(playerId)) {
            inputRequests(playerId)
        } else {
            null
        }
    }

    def answerInputRequest(playerId: Int, inputResponse : Int): Unit = {
        val request : InputRequest = inputRequests(playerId)
        if (request.answer.isDefined) {
            throw new Exception("Already answered request " + playerId)
        }
        if (request.choices.values.toList.contains(inputResponse)) {
            request.answer = Option(inputResponse)
            game.printer.print(Channel.Input, "Player " + playerId + " answered " + request.answer + " out of set " + request.choices)
        } else {
            request.answer = Option.empty
        }
    }

    def postOrGetInputRequest(playerId: Int,
                              requestType: InputRequestType.Value,
                              choices : Map[String, Int]): InputRequest = {
        if (choices.isEmpty) {
            game.printer.print(Channel.Debug, inputRequests.toString())
            throw new Exception("No valid answers supplied")
        }
        if (!inputRequests.contains(playerId)) {
            game.printer.print(Channel.Debug, "Created request for player " + playerId + " for " + requestType
                + " with answers " + choices)
            inputRequests += ((playerId, new InputRequest(playerId, requestType, choices)))
        }
        if (inputRequests(playerId).inputType != requestType) {
            assert(false)
        }
        inputRequests(playerId)
   }

    def removeInputRequest(playerId : Int): Unit = {
        inputRequests -= playerId
    }

    def clearPendingRequests(): Unit = {
        inputRequests.clear()
    }

    def getBooleanAnswers : Map[String, Int] = {
        Map("No" -> 0, "Yes" -> 1)
    }

    def getBooleanResponseFromInput(input : InputRequest) : Boolean = {
         input.answer.getOrElse(throw new RuntimeException("Input Request was not answered!")) == 1
    }

    def getPirateIdFromInput(input : InputRequest) : Int = {
        input.answer.getOrElse(throw new RuntimeException("Input Request was not answered!"))
    }

    def getBootyFromInput(input : InputRequest) : Booty.Value = {
        Booty(input.answer.getOrElse(throw new RuntimeException("Input Request was not answered!")))
    }

    def getTargetPirateFromInput(input : InputRequest) : Pirate = {
        if (input.inputType == InputRequestType.KillPirateInAnyDen || input.inputType == InputRequestType.KillPirateInAdjacentDen) {
            getPirateFromLocalizedPirateId(game.playerManager.getPlayer(
                 input.playerId),
                 input.answer.getOrElse(throw new RuntimeException("Input Request was not answered!")))
        } else {
            throw new IllegalStateException("Can't extract player from input " + input)
        }
    }

    def getPlayerHandFromPlayer(player: Player) : Map[String, Int] = {
        getPlayerPiratesInState(player, PirateState.Hand)
    }

    def getPlayerDiscardFromPlayer(player: Player) : Map[String, Int] = {
        getPlayerPiratesInState(player, PirateState.Discard)
    }

    def getPlayerDenFromPlayer(player: Player) : Map[String, Int] = {
        getPlayerPiratesInState(player, PirateState.Den)
    }

    def getPlayerPiratesInState(player : Player, state : PirateState.Value) : Map[String, Int] = {
        player.pirates
            .filter(p => p.state == state)
            .map(p => p.name -> p.rank) // why do I need -1?
            .toMap
    }

    def getBootyTypesOwnedByPlayer(player: Player) : Seq[Booty.Value] = {
        // Returns all of the types of booty the player currently possesses
        player.booty.keySet
            .filter(bootyType => player.booty(bootyType) > 0)
            .toSeq
    }

    def getSellableBootyTypesFromPlayer(player : Player) : Seq[Booty.Value] = {
        // 'Sellable' is defined as having 2 or more of the same type of booty
        player.booty
            .filter(entry => entry._2 >= 2)
            .keys
            .toSeq
    }

    def getAdjacentDenPirates(localPlayer : Player) : Map[String, Int] = {
        // Returns a list of playerId * 30 + pirateId
        game.playerManager.getAdjacentPlayers(localPlayer)
            .flatMap(p => p.pirates.filter(pirate => pirate.state == PirateState.Den))
            .map(pirate => pirate.tag -> getLocalizedPirateId(localPlayer, pirate))
            .toMap
    }


    def getAllDenPirates(localPlayer : Player) : Map[String, Int] = {
        // Returns a list of conjoined playerId + pirateId. localPlayer has no playerid
        game.playerManager.players
            .flatMap(player => player.pirates.filter(pirate => pirate.state == PirateState.Den))
            .map(pirate => pirate.tag -> getLocalizedPirateId(localPlayer, pirate))
            .toMap
    }

    /**
      * Gets the pirate's global identifier of playerId + pirateId from the perspective of localPlayer
      * @param localPlayer
      * @param pirate
      * @return
      */
    private def getLocalizedPirateId(localPlayer : Player, pirate : Pirate) : Int = {
        game.playerManager.getLocalPlayerId(localPlayer, pirate.player) * 30 + pirate.rank - 1
    }

    private def getPirateFromLocalizedPirateId(localPlayer : Player, localizedPirateId : Int) : Pirate = {
        val localizedPlayerId = localizedPirateId / 30
        val player = game.playerManager.getPlayerFromLocalPlayerId(localPlayer, localizedPlayerId)
        val pirateRank = localizedPirateId % 30 + 1

        player.getPirate(pirateRank)
    }
}
