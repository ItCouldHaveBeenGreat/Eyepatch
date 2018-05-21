package main.java.piratebot.pirates

import main.java.piratebot._

class Beggar(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 3
    val name = "Beggar"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val firstPlayer = round.dayStack.last.player
        val doubloonsToTake = Math.min(3, firstPlayer.doubloons)
        player.doubloons += doubloonsToTake
        firstPlayer.doubloons -= doubloonsToTake
        game.printer.print(Channel.Debug, tag + ": stole " + doubloonsToTake + " from player " + firstPlayer.playerId)
        RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(4, 3, 6, 5, 1, 2)(player.playerId)
    }
}