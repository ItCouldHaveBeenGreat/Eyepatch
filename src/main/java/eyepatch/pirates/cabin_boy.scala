package libertalia

import main._
import main.java.eyepatch._

class CabinBoy(player: Player) extends Pirate(player) {
    val rank = 5
    val name = "Cabin Boy"

    override def duskAction(round : Round): RetriableMethodResponse.Value = {
        OutputManager.print(Channel.Pirate, tag + ": Doesn't claim booty")
        return RetriableMethodResponse.Complete
    }

    def getSubRank(player : Player) : Int = {
        return Array(6, 5, 2, 1, 3, 4)(player.playerId);
    }
}