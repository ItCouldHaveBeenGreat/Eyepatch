package main.java.piratebot.pirates

import main.java.piratebot._

class FirstMate(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 28
    val name = "First Mate"

    override def endOfVoyageAction(): Unit = {
        val numDen = player.pirates.count( p => p.state == PirateState.Den)
        player.doubloons += numDen
        game.printer.print(Channel.Debug, tag + ": +" + numDen + " Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        Array(1, 6, 3, 2, 4, 5)(player.playerId)
    }
}