package main.java.piratebot.pirates

import main.java.piratebot._

class Gambler(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 24
    val name = "Gambler"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons -= player.booty.values.sum
        game.printer.print(Channel.Debug, tag + ": -" + player.booty.values.sum + " Doubloons")
        RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction(): Unit = {
        player.doubloons += 8
        game.printer.print(Channel.Debug, tag + ": +8 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        Array(4, 3, 6, 5, 1, 2)(player.playerId)
    }
}