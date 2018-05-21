package main.java.piratebot.pirates

import main.java.piratebot._

class Barkeep(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 7
    val name = "Barkeep"

    override def nightAction: RetriableMethodResponse.Value = {
        player.doubloons += 1
        game.printer.print(Channel.Debug, tag + ": +1 Doubloons")
        RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(1, 6, 3, 2, 4, 5)(player.playerId)
    }
}