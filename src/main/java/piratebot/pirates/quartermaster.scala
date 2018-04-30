package main.java.piratebot.pirates

import main.java.piratebot._

class Quartermaster(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 26
    val name = "Quartermaster"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons += player.booty.values.sum
        logger.debug(tag + ": +" + player.booty.values.sum + " Doubloons")
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction(): Unit = {
        player.doubloons -= 8
        logger.debug(tag + ": -8 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        Array(5, 4, 1, 6, 2, 3)(player.playerId)
    }
}