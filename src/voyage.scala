class Voyage(val numPlayers : Int) {
    var currentRound: Round = new Round(List(Booty.Goods))
    var roundsTaken : Int = 0;
    val totalRounds: Int = 6;
    
    PlayerManager.players.foreach( p => p.doubloons = 10)
    
    def makeProgress() : RetriableMethodResponse.Value = {
        var response = currentRound.makeProgress()
        if (response == RetriableMethodResponse.Complete) {
            println("Round " + roundsTaken + " complete")
            currentRound.endRound()
            roundsTaken += 1
            if (roundsTaken >= totalRounds) {
                return RetriableMethodResponse.Complete
            } else {
                currentRound = new Round(List(Booty.Goods))
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