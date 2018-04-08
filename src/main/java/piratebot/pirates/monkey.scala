package libertalia

import main._
import main.java.piratebot._

class Monkey(player: Player) extends Pirate(player) {
    val rank = 2
    val name = "Monkey"

   override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val leftPlayer = PlayerManager.getLeftPlayer(player)
        val numMasks = player.booty.count(b => b == Booty.CursedMask)
        if (numMasks > 0) {
            for (i <- 1 to numMasks) {
                leftPlayer.booty += Booty.CursedMask
                // TODO: There must be a better way to batch remove
                player.booty -= Booty.CursedMask
            }
            OutputManager.print(Channel.Pirate, tag + ": transferred all Cursed Masks to player " + leftPlayer.playerId)
        } else {
            OutputManager.print(Channel.Pirate, tag + ": No Cursed Masks to transfer")
        }
        
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(3, 2, 5, 4, 2, 1)(player.playerId);
    }
}