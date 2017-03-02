import scala.collection.mutable.ArrayBuffer

class Round() {
    
    var state : RoundState.Value = RoundState.SolicitPirates
    val pirates : Seq[Pirate] = ArrayBuffer[Pirate]()
    
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
        var pendingInput : Boolean = false
        for (player <- PlayerManager.instance.players) {
            val request = InputManager.postAndGetInputRequest(player.playerId, InputRequestType.PlayPirateFromHand, List("25", "29"))
            if (request.answer == "") {
                pendingInput = true
            }
        }
        if (pendingInput) {
            println("Waiting for players to select pirates")
            return RetriableMethodResponse.PendingInput
        } else {
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
