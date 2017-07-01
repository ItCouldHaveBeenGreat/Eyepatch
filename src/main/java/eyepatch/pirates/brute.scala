package libertalia

import main._
import main.java.eyepatch._

class Brute(player: Player) extends Pirate(player) {
    val rank = 14
    val name = "Brute"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val pirateToKill = round.dayStack.last
        round.killPirate(pirateToKill)
        OutputManager.print(Channel.Pirate, tag + ": killed " + pirateToKill.tag)
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(1, 6, 3, 2, 4, 5)(player.playerId);
    }
}