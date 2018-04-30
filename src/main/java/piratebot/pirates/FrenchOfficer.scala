package libertalia

import main._
import main.java.piratebot._

class FrenchOfficer(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 10
    val name = "French Officer"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        if (player.doubloons <= 9) {
            logger.debug(tag + ": +5 Doubloons")
            player.doubloons += 5
        } else {
            logger.debug(tag + ": did nothing")
        }
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(4, 3, 6, 5, 1, 2)(player.playerId)
    }
}