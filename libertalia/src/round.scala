import scala.collection.mutable.ArrayBuffer

class Round(val booty : ArrayBuffer[Booty.Value]) {
    
    var state : RoundState.Value = RoundState.SolicitPirates
    
    var dayStack = ArrayBuffer[Pirate]()
    private var duskStack = ArrayBuffer[Pirate]()
    private var survivorStack = ArrayBuffer[Pirate]()
    private var nightSequences = List[ArrayBuffer[Pirate]]()
    
    
    def makeProgress() : RetriableMethodResponse.Value = {
        state match {
            case RoundState.SolicitPirates => {
                val response = solicitPirates()
                if (response == RetriableMethodResponse.Complete) {
                    state = RoundState.DayActions;
                    return RetriableMethodResponse.MadeProgress
                } else {
                    return response
                }
            }
            case RoundState.DayActions => {
                val response = performDayActions()
                if (response == RetriableMethodResponse.Complete) {
                    state = RoundState.DuskActions;
                    return RetriableMethodResponse.MadeProgress
                } else {
                    return response
                }
            }
            case RoundState.DuskActions => {
                val response = performDuskActions()
                if (response == RetriableMethodResponse.Complete) {
                    state = RoundState.NightActions;
                    prepareForNightActions
                    return RetriableMethodResponse.MadeProgress
                } else {
                    return response
                }
            }
            case RoundState.NightActions => {
                val response = performNightActions()
                if (response == RetriableMethodResponse.Complete) {
                    return RetriableMethodResponse.Complete;   
                } else {
                    return response
                }
            }
        }
    }

    private def solicitPirates() : RetriableMethodResponse.Value = {
        val requests : ArrayBuffer[InputRequest] = ArrayBuffer()
        for (p <- PlayerManager.players) {
            requests += InputManager.postAndGetInputRequest(
                p.playerId,
                InputRequestType.PlayPirateFromHand,
                InputManager.getPlayerHandFromPlayer(p))
        }

        var pendingInput : Boolean = false
        for (request <- requests) {
            if (!request.answered) {
                pendingInput = true
            }
        }

        if (pendingInput) {
            println("Waiting for players to select pirates")
            return RetriableMethodResponse.PendingInput
        } else {
            for (request <- requests) {
                val pirateRank = InputManager.getPirateIdFromInput(request)
                val pirateToAdd = PlayerManager.players(request.playerId).getPirate(pirateRank)
                addPirate(pirateToAdd)
                computevisibility(PlayerManager.players(request.playerId))
                println("Player " + request.playerId + " played " + pirateToAdd.name)
                InputManager.removeInputRequest(request.playerId)
            }
        }
        return RetriableMethodResponse.Complete
    
    }
    
    def addPirate(pirate : Pirate) = {
        // right now pirates can only be added during the day
        pirate.state = PirateState.Board
        dayStack += pirate
        dayStack = dayStack.sorted
    }

    def computevisibility(player : Player) {
        // if the player's hand has no unknowns, the graveyard returns to a fully known state
        if (player.pirates.count( p => p.state == PirateState.Hand && p.known == true ) == 0) {
            for (p <- player.pirates.filter ( p => p.state == PirateState.Discard )) {
                p.known = false
            }
        }
    }

    def killPirate(pirate : Pirate) = {
        pirate.state = PirateState.Discard
        dayStack -= pirate
        duskStack -= pirate
    }
    
    private def performDayActions() : RetriableMethodResponse.Value = {
        while (dayStack.size > 0) {
            val activePirate = dayStack.head
            println("Round running day action for " + activePirate.tag)
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
        return RetriableMethodResponse.Complete
    }
    
    private def performDuskActions() : RetriableMethodResponse.Value = {
         while (duskStack.size > 0) {
            val activePirate = duskStack.head
            println("Round running dusk action for " + activePirate.tag)
            val response = activePirate.duskAction(this)

            if (response != RetriableMethodResponse.Complete) {
                return response // We're pending something; return
            } else if (activePirate.state == PirateState.Board) {
                // If the active pirate is still alive, move them to the survivorStack
                survivorStack += activePirate
                duskStack -= activePirate
            }
        }
        
        println("Dusk over; all surviving pirates move to their dens")
        survivorStack.foreach( p => p.state = PirateState.Den)
        return RetriableMethodResponse.Complete
    }
    
    private def prepareForNightActions() = {
        // pirates are resolved in descending order during night
        nightSequences = PlayerManager.players.map( player => 
            player.pirates.filter( p => p.state == PirateState.Den ).sorted(Ordering[Pirate].reverse).to[ArrayBuffer]
        )
    }
    
    private def performNightActions() : RetriableMethodResponse.Value = {
        nightSequences.foreach ( nightSequence =>
            while (nightSequence.size > 0) {
                val response = nightSequence.head.nightAction
                if (response != RetriableMethodResponse.Complete) {
                    return response // We're pending something
                } else {
                    nightSequence -= nightSequence.head
                }
            } 
        )
        return RetriableMethodResponse.Complete
    }
    
    def endRound() = {

    }
}

object RoundState extends Enumeration {
    type RoundState = Value
    val SolicitPirates, DayActions, DuskActions, NightActions = Value
}
