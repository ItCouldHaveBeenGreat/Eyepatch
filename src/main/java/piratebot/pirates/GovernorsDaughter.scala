package libertalia

import main._
import main.java.piratebot._

class GovernorsDaughter(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 25
    val name = "Governor's Daughter"

    override def endOfVoyageAction(): Unit = {
        if (otherGovernorsDaughter) {
            player.doubloons -= 3
            game.printer.print(Channel.Debug, tag + ": -3 Doubloons")
        } else {
            player.doubloons += 6
            game.printer.print(Channel.Debug, tag + ": +6 Doubloons")
        }
    }
    
    private def otherGovernorsDaughter : Boolean = {
        for (p <- game.playerManager.players) {
            if (p.getPirate(rank).state == PirateState.Den) {
                true
            }
        }
        false
    }
    
    def getSubRank(player : Player) : Int = {
        Array(4, 3, 6, 5, 1, 2)(player.playerId)
    }
}