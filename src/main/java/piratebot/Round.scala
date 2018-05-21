package main.java.piratebot

import com.rits.cloning.Immutable

import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

class Round(game: Game, val booty : ArrayBuffer[Booty.Value]) {
    var state : RoundState.Value = RoundState.SolicitPirates

    var dayStack: ArrayBuffer[Pirate] = ArrayBuffer[Pirate]()
    private var duskStack = ArrayBuffer[Pirate]()
    private var survivorStack = ArrayBuffer[Pirate]()
    private var nightSequences = Seq[ArrayBuffer[Pirate]]()


    def makeProgress() : RetriableMethodResponse.Value = {
        state match {
            case RoundState.SolicitPirates => {
                val response = solicitPirates()
                if (response == RetriableMethodResponse.Complete) {
                    state = RoundState.DayActions
                    RetriableMethodResponse.MadeProgress
                } else {
                    response
                }
            }
            case RoundState.DayActions => {
                val response = performDayActions()
                if (response == RetriableMethodResponse.Complete) {
                    state = RoundState.DuskActions
                    RetriableMethodResponse.MadeProgress
                } else {
                    response
                }
            }
            case RoundState.DuskActions => {
                val response = performDuskActions()
                if (response == RetriableMethodResponse.Complete) {
                    state = RoundState.NightActions
                    prepareForNightActions()
                    RetriableMethodResponse.MadeProgress
                } else {
                    response
                }
            }
            case RoundState.NightActions => {
                val response = performNightActions()
                if (response == RetriableMethodResponse.Complete) {
                    RetriableMethodResponse.Complete
                } else {
                    response
                }
            }
        }
    }

    private def solicitPirates() : RetriableMethodResponse.Value = {
        val requests : ArrayBuffer[InputRequest] = ArrayBuffer()
        for (p <- game.playerManager.players) {
            requests += game.inputManager.postAndGetInputRequest(
                p.playerId,
                InputRequestType.PlayPirateFromHand,
                game.inputManager.getPlayerHandFromPlayer(p))
        }

        var pendingInput : Boolean = false
        for (request <- requests) {
            if (request.answer.isEmpty) {
                pendingInput = true
            }
        }

        if (pendingInput) {
            game.printer.print(Channel.Debug, "Waiting for players to select main.pirates")
            return RetriableMethodResponse.PendingInput
        } else {
            for (request <- requests) {
                val pirateRank = game.inputManager.getPirateIdFromInput(request)
                println("ORANGE: " + request.playerId)
                println("ORANGE: " + request.inputType.toString)
                println("ORANGE: " + request.choices.toString)
                val pirateToAdd = game.playerManager.players(request.playerId).getPirate(pirateRank)

                addPirate(pirateToAdd)
                computeVisibility(game.playerManager.players(request.playerId))
                game.printer.print(Channel.Debug, "Player " + request.playerId + " played " + pirateToAdd.name)
                game.inputManager.removeInputRequest(request.playerId)
            }
        }
        RetriableMethodResponse.Complete
    
    }
    
    def addPirate(pirate : Pirate): Unit = {
        // right now main.pirates can only be added during the day
        pirate.state = PirateState.Board
        dayStack += pirate
        dayStack = dayStack.sorted
    }

    def computeVisibility(player : Player) {
        // if the player's hand has no unknowns, the graveyard returns to a fully known state
        if (player.pirates.count( p => p.state == PirateState.Hand && p.known ) == 0) {
            for (p <- player.pirates.filter ( p => p.state == PirateState.Discard )) {
                p.known = false
            }
        }
    }

    def killPirate(pirate : Pirate): Unit = {
        pirate.state = PirateState.Discard
        dayStack -= pirate
        // NOTE: Pirates cannot be killed /during/ the dusk phaase (cook + spanish spy interaction)
        //duskStack -= pirate
    }
    
    private def performDayActions() : RetriableMethodResponse.Value = {
        while (dayStack.nonEmpty) {
            val activePirate = dayStack.head
            game.printer.print(Channel.Debug, "Round running day action for " + activePirate.tag)
            val response = activePirate.dayAction(this)

            if (response != RetriableMethodResponse.Complete) {
                return response // We're pending something; return
            } else if (activePirate.state == PirateState.Board) {
                // If the active pirate is still alive, move them to the duskStack
                duskStack += activePirate
                dayStack -= activePirate
            }
        }
        duskStack = duskStack.sorted(Ordering[Pirate].reverse)
        RetriableMethodResponse.Complete
    }
    
    private def performDuskActions() : RetriableMethodResponse.Value = {
         while (duskStack.nonEmpty) {
            val activePirate = duskStack.head
             game.printer.print(Channel.Debug, "Round running dusk action for " + activePirate.tag)
            val response = activePirate.duskAction(this)
            if (response != RetriableMethodResponse.Complete) {
                return response // We're pending something; return
            } else {
                if (activePirate.state == PirateState.Board) {
                    // If the active pirate is still alive, move them to the survivorStack
                    survivorStack += activePirate
                }
                duskStack -= activePirate
            }
        }

        game.printer.print(Channel.Debug, "Dusk over; all surviving main.pirates move to their dens")
        survivorStack.foreach( p => p.state = PirateState.Den)
        RetriableMethodResponse.Complete
    }
    
    private def prepareForNightActions(): Unit = {
        // main.pirates are resolved in descending order during night
        nightSequences = game.playerManager.players.map( player =>
            player.pirates.filter( p => p.state == PirateState.Den ).sorted(Ordering[Pirate].reverse).to[ArrayBuffer]
        )
    }
    
    private def performNightActions() : RetriableMethodResponse.Value = {
        nightSequences.foreach ( nightSequence =>
            while (nightSequence.nonEmpty) {
                val response = nightSequence.head.nightAction
                if (response != RetriableMethodResponse.Complete) {
                    return response // We're pending something
                } else {
                    nightSequence -= nightSequence.head
                }
            } 
        )
        RetriableMethodResponse.Complete
    }
}

@Immutable
object RoundState extends Enumeration {
    type RoundState = Value
    val SolicitPirates, DayActions, DuskActions, NightActions = Value
}
