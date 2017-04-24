import scala.collection.mutable.ArrayBuffer

class Round(val booty : ArrayBuffer[Booty.Value]) {
    
    var state : RoundState.Value = RoundState.SolicitPirates
    var pirates : ArrayBuffer[Pirate] = ArrayBuffer[Pirate]()
    
    // TODO: Null is bad
    private var dayIterator : BufferedIterator[Pirate] = null
    private var duskIterator : BufferedIterator[Pirate] = null
    private var nightIterators : List[BufferedIterator[Pirate]] = null
   
   println("Available booty: " + booty)
    
    def makeProgress() : RetriableMethodResponse.Value = {
        state match {
            case RoundState.SolicitPirates => {
                val response = solicitPirates()
                if (response == RetriableMethodResponse.Complete) {
                    state = RoundState.DayActions;
                    prepareForDayActions()
                    return RetriableMethodResponse.MadeProgress
                } else {
                    return response
                }
            }
            case RoundState.DayActions => {
                val response = performDayActions()
                if (response == RetriableMethodResponse.Complete) {
                    state = RoundState.DuskActions;
                    prepareForDuskActions()
                    return RetriableMethodResponse.MadeProgress
                } else {
                    return response
                }
            }
            case RoundState.DuskActions => {
                val response = performDuskActions()
                if (response == RetriableMethodResponse.Complete) {
                    state = RoundState.NightActions;
                    prepareForNightActions()
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
        pirates += pirate
        pirate.state = PirateState.Board
        pirates = pirates.sorted
        dayIterator = pirates.toIterator.buffered
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
        pirates -= pirate
        pirate.state = PirateState.Discard
    }
    
    private def prepareForDayActions() = {
        dayIterator = pirates.toIterator.buffered
    }
    
    private def performDayActions() : RetriableMethodResponse.Value = {
        while (dayIterator.hasNext) {
            println("Round running day action for " + dayIterator.head.tag)
            val response = dayIterator.head.dayAction(this)
            if (response != RetriableMethodResponse.Complete) {
                return response // We're pending something
            } else {
                if (dayIterator.hasNext) {
                    dayIterator.next // Advance to next pirate
                } else {
                    // CASE: Brute can kill themselves, causing this edge case
                    return RetriableMethodResponse.Complete
                }
            }
        }
        return RetriableMethodResponse.Complete
    }
    
    private def prepareForDuskActions() = {
        duskIterator = pirates.reverseIterator.buffered
    }
    
    private def performDuskActions() : RetriableMethodResponse.Value = {
        // TODO: Got a lot of repeated code here
        while (duskIterator.hasNext) {
            println("Round running dusk action for " + duskIterator.head.tag)
            val response = duskIterator.head.duskAction(this)
            if (response != RetriableMethodResponse.Complete) {
                return response // We're pending something
            } else {
                duskIterator.next // Advance to next pirate
            }
        }
        return RetriableMethodResponse.Complete
    }
    
    private def prepareForNightActions() = {
        pirates.foreach ( p => p.state = PirateState.Den)

        // NOTE: Mutineer must resolve last due to its capacity to discard other pirates
        val pirateSets = PlayerManager.players.map( player =>
            player.pirates.filter( pirate =>
                pirate.state == PirateState.Den && pirate.rank != 13
            )
        )
        nightIterators = pirateSets.map( set => set.iterator.buffered )
    }
    
    private def performNightActions() : RetriableMethodResponse.Value = {
        nightIterators.foreach ( nightIterator =>
            while (nightIterator.hasNext) {
                val response = nightIterator.head.nightAction
                if (response != RetriableMethodResponse.Complete) {
                    return response // We're pending something
                } else {
                    nightIterator.next // Advance to next pirate
                }
            } 
        )
        
        // Actually resolve the mutineer now
        val mutineers = PlayerManager.players.map( player => player.getPirate(13) )
                                             .filter( pirate => pirate.state == PirateState.Den )
        mutineers.foreach( mutineer => mutineer.nightAction )

        return RetriableMethodResponse.Complete
    }
    
    def endRound() = {

    }
}

object RoundState extends Enumeration {
    type RoundState = Value
    val SolicitPirates, DayActions, DuskActions, NightActions = Value
}
