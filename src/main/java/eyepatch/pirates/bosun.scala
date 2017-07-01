package libertalia

import main._
import main.java.eyepatch._

class Bosun(player: Player) extends Pirate(player) {
    val rank = 19
    val name = "Bosun"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val numLowRank = player.pirates.count( p => p.state == PirateState.Den && p.rank < rank)
        player.doubloons += 2 * numLowRank
        OutputManager.print(Channel.Pirate, tag + ": +" + numLowRank * 2 + " Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(5, 4, 1, 6, 2, 3)(player.playerId);
    }
}