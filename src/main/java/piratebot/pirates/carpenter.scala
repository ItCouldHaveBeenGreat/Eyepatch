package main.java.piratebot.pirates

import main.java.piratebot._

class Carpenter(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 9
    val name = "Carpenter"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons = player.doubloons / 2 + player.doubloons % 2
        logger.debug(tag + ": -50% Doubloons")
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction(): Unit = {
        player.doubloons += 10
        logger.debug(tag + ": +10 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        Array(3, 2, 5, 4, 6, 1)(player.playerId)
    }
}