import scala.collection.mutable.ArrayBuffer

class Round(val booty : Seq[Booty.Value]) {
    
    var state : RoundState.Value = RoundState.SolicitPirates
    val pirates : ArrayBuffer[Pirate] = ArrayBuffer[Pirate]()
    
    def makeProgress() : RetriableMethodResponse.Value = {
        if (state == RoundState.SolicitPirates) {
            var response = solicitPirates()
            if (response != RetriableMethodResponse.Complete) {
                return response
            }
        } else if (state == RoundState.DayActions) { 
            // order pirates in ascending order
        } else if (state == RoundState.DuskActions) {
            // sort pirates in descending order
        } else if (state == RoundState.NightActions) {
            //
        }
        
        return RetriableMethodResponse.Complete;   
    }
  
    // only repeat actions which can be repeated
    // the only actions which require repeats are those which have player input
    // each code block should only contain the repeatable aspects
    // only code repeats for situations which can have them (inputs)

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
            } else {
                val pirateRank = InputManager.getPirateIdFromInput(request)
                val pirateToAdd = PlayerManager.players(request.playerId).getPirate(pirateRank)
                pirateToAdd.state = PirateState.InPlay
                pirates += pirateToAdd
            }
        }
        
        if (pendingInput) {
            println("Waiting for players to select pirates")
            return RetriableMethodResponse.PendingInput
        } else {
            // Sort ascending for day time
            pirates.sorted
            state = RoundState.DayActions;
            return RetriableMethodResponse.Complete
        }
        
    }
    
    private def performDayActions() : RetriableMethodResponse.Value = {
        // return RetriableMethodResponse.PendingInput
        state = RoundState.DuskActions
        return RetriableMethodResponse.Complete
    }
    
    private def performDuskActions() : RetriableMethodResponse.Value = {
        // return RetriableMethodResponse.PendingInput
        state = RoundState.NightActions
        return RetriableMethodResponse.Complete
    }
    
    private def performNightActions() : RetriableMethodResponse.Value = {
        return RetriableMethodResponse.Complete
    }
    
    def endRound() = {
        
    }
}

object RoundState extends Enumeration {
    type RoundState = Value
    val SolicitPirates, DayActions, DuskActions, NightActions = Value
}
