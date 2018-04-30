package main.java.piratebot.pirates

import main.java.piratebot._

class VoodooWitch(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 11
    val name = "Voodoo Witch"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val numDiscard = player.pirates.count( p => p.state == PirateState.Discard )
        player.doubloons += 2 * numDiscard
        logger.debug(tag + ": +" + numDiscard * 2 + " Doubloons")
        RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        Array(5, 4, 1, 6, 2, 3)(player.playerId)
    }
}