package main.java.piratebot.pirates

import main.java.piratebot._

class Treasurer(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 23
    val name = "Treasurer"

    override def endOfVoyageAction(): Unit = {
        val numCommodities = player.booty(Booty.Goods)
            + player.booty(Booty.Jewels)
            + player.booty(Booty.Chest)
        player.doubloons += numCommodities
        game.printer.print(Channel.Debug, tag + ": +" + numCommodities + " Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        Array(3, 2, 5, 4, 6, 1)(player.playerId)
    }
}