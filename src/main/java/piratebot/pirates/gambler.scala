package libertalia

import main._
import main.java.piratebot._

class Gambler(player: Player) extends Pirate(player) {
    val rank = 24
    val name = "Gambler"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons -= player.booty.values.sum
        OutputManager.print(Channel.Pirate, tag + ": -" + player.booty.values.sum + " Doubloons")
        RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction = {
        player.doubloons += 8
        OutputManager.print(Channel.Pirate, tag + ": +8 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        Array(4, 3, 6, 5, 1, 2)(player.playerId);
    }
}