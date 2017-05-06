package libertalia

import scala.collection.mutable.ArrayBuffer

class Voyage(val numPlayers : Int) {
    val totalRounds: Int = 6;
    
    BootyBag.build
    val bootySets = ArrayBuffer.fill(totalRounds)( ArrayBuffer.fill(numPlayers)( BootyBag.draw ) )
    var currentRound = new Round(bootySets(0))
    var roundsTaken : Int = 0;

    PlayerManager.players.foreach( p => p.doubloons = 10)
    
    def makeProgress() : RetriableMethodResponse.Value = {
        var response = currentRound.makeProgress()
        if (response == RetriableMethodResponse.Complete) {
            OutputManager.print(Channel.Game, "Round " + roundsTaken + " complete")
            roundsTaken += 1
            if (roundsTaken >= totalRounds) {
                endVoyage()
                return RetriableMethodResponse.Complete
            } else {
                currentRound = new Round(bootySets(roundsTaken))
                return RetriableMethodResponse.MadeProgress
            }
        } else {
            // Pass response up if PendingInput or MadeProgress
            return response
        }
    }
    
    def endVoyage() = {
        PlayerManager.players.foreach ( p => p.endVoyage )
    }
}