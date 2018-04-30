package main.java.piratebot.pirates

import main.java.piratebot._

class FreedSlave(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 12
    val name = "Freed Slave"

    override def nightAction: RetriableMethodResponse.Value = {
        val numHighRank = player.pirates.count( p => p.state == PirateState.Den && p.rank > rank)
        player.doubloons += numHighRank
        logger.debug(tag + ": +" + numHighRank + " Doubloons")
        RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        Array(6, 5, 2, 1, 3, 4)(player.playerId)
    }
}