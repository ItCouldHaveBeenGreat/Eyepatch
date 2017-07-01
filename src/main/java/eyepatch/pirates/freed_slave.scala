package libertalia

import main._
import main.java.eyepatch._

class FreedSlave(player: Player) extends Pirate(player) {
    val rank = 12
    val name = "Freed Slave"

    override def nightAction: RetriableMethodResponse.Value = {
        val numHighRank = player.pirates.count( p => p.state == PirateState.Den && p.rank > rank)
        player.doubloons += numHighRank
        OutputManager.print(Channel.Pirate, tag + ": +" + numHighRank + " Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(6, 5, 2, 1, 3, 4)(player.playerId);
    }
}