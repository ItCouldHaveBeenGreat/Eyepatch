class Voyage() {
    var currentRound: Round = new Round()
    var roundsTaken : Int = 0;
    val totalRounds: Int = 6;
    
    
    def makeProgress() : RetriableMethodResponse.Value = {
        var response = currentRound.makeProgress()
        if (response == RetriableMethodResponse.Complete) {
            println("Round " + roundsTaken + " complete")
            currentRound.endRound()
            roundsTaken += 1
            if (roundsTaken >= totalRounds) {
                return RetriableMethodResponse.Complete
            } else {
                currentRound = new Round()
                return RetriableMethodResponse.MadeProgress
            }
        } else {
            // Pass response up if PendingInput or MadeProgress
            return response
        }
    }
    
    def endVoyage() = {
        
    }
}