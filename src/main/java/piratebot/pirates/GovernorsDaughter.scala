package libertalia

import main._
import main.java.piratebot._

class GovernorsDaughter(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 25
    val name = "Governor's Daughter"

    override def endOfVoyageAction = {
        if (otherGovernorsDaughter) {
            player.doubloons -= 3
            logger.debug(tag + ": -3 Doubloons")
        } else {
            player.doubloons += 6
            logger.debug(tag + ": +6 Doubloons")
        }
    }
    
    private def otherGovernorsDaughter : Boolean = {
        for (p <- game.playerManager.players) {
            if (p.getPirate(rank).state == PirateState.Den) {
                return true;
            }
        }
        return false;
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(4, 3, 6, 5, 1, 2)(player.playerId);
    }
}