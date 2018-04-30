package main.java.piratebot.pirates

import main.java.piratebot._

class Topman(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 16
    val name = "Topman"

    override def endOfVoyageAction(): Unit = {
        // If there are no dens which are explicitly smaller, award bonus
        // NOTE: Allows ties
        val smallerDenCounts = game.playerManager.players.map( p => p.pirates.size )
                                            .filter( count => count < player.pirates.size)
        if (smallerDenCounts.isEmpty) {
            player.doubloons += 5
            logger.debug(tag + ": +5 Doubloons")
        } else {
            logger.debug(tag + ": No bonus")
        }
    }
    
    def getSubRank(player : Player) : Int = {
        Array(3, 2, 5, 4, 6, 1)(player.playerId)
    }
}