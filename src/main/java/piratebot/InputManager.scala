package main.java.piratebot

import scala.collection.mutable.{HashMap, Map}

// TODO: Define class as the intersection of a player facing trait and a game facing trait
object InputManager {

    // TODO: Restructure this to be a set
    private val inputRequests : Map[Int, InputRequest] = HashMap[Int, InputRequest]()
    private var nextRequestId : Int = 0;


    def getInputRequest(playerId : Int) : InputRequest = {
        if (inputRequests.contains(playerId)) {
            return inputRequests(playerId)
        } else {
            return null
        }
    }

    def answerInputRequest(playerId: Int, inputResponse : String) = {
        val request : InputRequest = inputRequests(playerId)
        if (request.answered) {
            throw new Exception("Already answered request " + playerId)
        }
        if (request.validAnswers.contains(inputResponse)) {
            request.answer = inputResponse
            request.answered = true
            OutputManager.print(Channel.Debug, "Player " + playerId + " answered " + request.answer + " out of set " + request.validAnswers)
        } else {
            throw new Exception("Invalid answer for request " + playerId)
        }
    }

    def postAndGetInputRequest(playerId: Int,
                               requestType: InputRequestType.Value,
                               validAnswers : Seq[String]) : InputRequest = {
        if (validAnswers.size == 0) {
            throw new Exception("No valid answers supplied")
        }
        if (!inputRequests.contains(playerId)) {
            OutputManager.print(Channel.Debug, "Created request for player " + playerId + " for " + requestType
                + "with answers " + validAnswers)
            inputRequests += ((playerId, new InputRequest(playerId, requestType, validAnswers)))
        }
        return inputRequests(playerId)
   }

    def removeInputRequest(playerId : Int) = {
        inputRequests -= playerId
    }

    def getBooleanAnswers : Seq[String] = {
        return List("0", "1")
    }

    def getBooleanResponseFromInput(input : InputRequest) : Boolean = {
        return input.answer == "0"
    }

    def getPirateIdFromInput(input : InputRequest) : Int = {
        return input.answer.toInt
    }

    def getBootyFromInput(input : InputRequest) : Booty.Value = {
        return Booty(input.answer.toInt)
    }

    def getTargetPirateFromInput(input : InputRequest) : Pirate = {
        if (input.inputType == InputRequestType.KillPirateInAnyDen || input.inputType == InputRequestType.KillPirateInAdjacentDen) {
             return getPirateFromLocalizedPirateId(PlayerManager.getPlayer(input.playerId), input.answer)
        } else {
            throw new IllegalStateException("Can't extract player from input " + input)
        }
    }

    def getPlayerHandFromPlayer(player: Player) : Seq[String] = {
        return getPlayerPiratesInState(player, PirateState.Hand)
    }

    def getPlayerDiscardFromPlayer(player: Player) : Seq[String] = {
        return getPlayerPiratesInState(player, PirateState.Discard)
    }

    def getPlayerDenFromPlayer(player: Player) : Seq[String] = {
        return getPlayerPiratesInState(player, PirateState.Den)
    }

    def getPlayerPiratesInState(player : Player, state : PirateState.Value) : Seq[String] = {
        return player.pirates.filter( p => p.state == state ).map( p => p.rank.toString )
    }

    def getBootyFromPlayer(player: Player) : Seq[String] = {
        return player.booty.map(b => b.id.toString).distinct
    }

    def getSellableBootyFromPlayer(player : Player) : Seq[String] = {
        // 'Sellable' is defined as having 2 or more of the same type of booty
        // This is exclusively used by the Merchant
        return Booty.values.filter( bootyType => player.booty.count( playerBooty => playerBooty == bootyType ) >= 2 ).toList.map( b => b.id.toString )
    }

    def getAdjacentDenPirates(player : Player) : Seq[String] = {
        // Returns a list of conjoined playerId + pirateId
        // TODO: This is terrible, find a better way to address multiple players
        return PlayerManager.getAdjacentPlayers(player)
            .flatMap(p => p.pirates.filter(pirate => pirate.state == PirateState.Den)
            .map(pirate => getLocalizedPirateId(player, pirate)))
    }


    def getAllDenPirates(localPlayer : Player) : Seq[String] = {
        // Returns a list of conjoined playerId + pirateId. localPlayer has no playerid
        return PlayerManager.players
            .flatMap(player => player.pirates.filter(pirate => pirate.state == PirateState.Den)
            .map(pirate => getLocalizedPirateId(localPlayer, pirate)))
    }

    /**
      * Gets the pirate's global identifier of playerId + pirateId from the perspective of localPlayer
      * @param localPlayer
      * @param pirate
      * @return
      */
    private def getLocalizedPirateId(localPlayer : Player, pirate : Pirate) : String = {
        if (pirate.player == localPlayer) {
            pirate.rank.toString
        } else {
            PlayerManager.getLocalPlayerId(localPlayer, pirate.player).toString + "%02d".format(pirate.rank)
        }
    }

    private def getPirateFromLocalizedPirateId(localPlayer : Player, localizedPirateId : String) : Pirate = {
        if (localizedPirateId.length == 3) {
          val player = PlayerManager.getPlayerFromLocalPlayerId(localPlayer, localizedPirateId.substring(0, 1).toInt)
          return player.getPirate(localizedPirateId.substring(1).toInt)
        } else {
          return localPlayer.getPirate(localizedPirateId.toInt)
        }
    }
}
