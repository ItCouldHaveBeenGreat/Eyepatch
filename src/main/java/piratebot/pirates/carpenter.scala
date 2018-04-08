package libertalia

import main._
import main.java.piratebot._

class Carpenter(player: Player) extends Pirate(player) {
    val rank = 9
    val name = "Carpenter"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons = player.doubloons / 2 + player.doubloons % 2
        OutputManager.print(Channel.Pirate, tag + ": -50% Doubloons")
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction = {
        player.doubloons += 10
        OutputManager.print(Channel.Pirate, tag + ": +10 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(3, 2, 5, 4, 6, 1)(player.playerId);
    }
}