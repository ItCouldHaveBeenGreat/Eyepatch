package main.java.piratebot.pirates

import main.java.piratebot._

class Monkey(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 2
    val name = "Monkey"

   override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val leftPlayer = game.playerManager.getLeftPlayer(player)
        if (player.booty(Booty.CursedMask) > 0) {
            leftPlayer.booty(Booty.CursedMask) += player.booty(Booty.CursedMask)
            player.booty(Booty.CursedMask) = 0
            logger.debug(tag + ": transferred all Cursed Masks to player " + leftPlayer.playerId)
        } else {
            logger.debug(tag + ": No Cursed Masks to transfer")
        }
       RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(3, 2, 5, 4, 2, 1)(player.playerId)
    }
}