package main.java.piratebot.pirates

import main.java.piratebot._

class Captain(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 29
    val name = "Captain"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons += 3
        logger.debug(tag + ": +3 Doubloons")
        RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction() : Unit = {
        val numCursedRelics = player.booty(Booty.CursedMask)
        player.doubloons -= numCursedRelics * 3
        logger.debug(tag + ": -" + numCursedRelics * 3 + " Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        Array(2, 1, 4, 3, 5, 6)(player.playerId)
    }
}