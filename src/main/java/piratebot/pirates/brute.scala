package main.java.piratebot.pirates

import main.java.piratebot._

class Brute(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 14
    val name = "Brute"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val pirateToKill = round.dayStack.last
        round.killPirate(pirateToKill)
        logger.debug(tag + ": killed " + pirateToKill.tag)
        RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(1, 6, 3, 2, 4, 5)(player.playerId)
    }
}