package main.java.piratebot.pirates

import main.java.piratebot._

class CabinBoy(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 5
    val name = "Cabin Boy"

    override def duskAction(round : Round): RetriableMethodResponse.Value = {
        logger.debug(tag + ": Doesn't claim booty")
        RetriableMethodResponse.Complete
    }

    def getSubRank(player : Player) : Int = {
         Array(6, 5, 2, 1, 3, 4)(player.playerId)
    }
}