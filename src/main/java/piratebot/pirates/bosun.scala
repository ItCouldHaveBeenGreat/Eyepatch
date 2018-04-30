package main.java.piratebot.pirates

import main.java.piratebot._

class Bosun(game: Game, player: Player) extends Pirate(game, player) {

    val rank = 19
    val name = "Bosun"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val numLowRank = player.pirates.count( p => p.state == PirateState.Den && p.rank < rank)
        player.doubloons += 2 * numLowRank
        logger.debug(tag + ": +" + numLowRank * 2 + " Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        Array(5, 4, 1, 6, 2, 3)(player.playerId)
    }
}