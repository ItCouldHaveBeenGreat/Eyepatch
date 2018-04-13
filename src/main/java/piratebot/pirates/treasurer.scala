package libertalia

import main._
import main.java.piratebot._

class Treasurer(player: Player) extends Pirate(player) {
    val rank = 23
    val name = "Treasurer"

    override def endOfVoyageAction: Unit = {
        val numCommodities = player.booty(Booty.Goods)
            + player.booty(Booty.Jewels)
            + player.booty(Booty.Chest)
        player.doubloons += numCommodities
        OutputManager.print(Channel.Pirate, tag + ": +" + numCommodities + " Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(3, 2, 5, 4, 6, 1)(player.playerId);
    }
}