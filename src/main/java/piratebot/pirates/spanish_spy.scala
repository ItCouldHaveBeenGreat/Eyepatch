package libertalia

import main._
import main.java.piratebot._

class SpanishSpy(player: Player) extends Pirate(player) {
    val rank = 17
    val name = "SpanishSpy"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        for (_ <- 1 to player.booty(Booty.SpanishOfficer)) {
            val bootyDrawn = BootyBag.draw
            player.booty(bootyDrawn) += 1
            OutputManager.print(Channel.Pirate, tag + ": Drew a " + bootyDrawn)
        }
        player.booty(Booty.SpanishOfficer) = 0
        
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(4, 3, 6, 5, 1, 2)(player.playerId);
    }
}