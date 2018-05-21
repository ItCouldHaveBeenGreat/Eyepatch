package main.java.piratebot.pirates

import main.java.piratebot._

class Armorer(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 20
    val name = "Armorer"

    override def nightAction: RetriableMethodResponse.Value = {
        val numSabers = player.booty(Booty.Saber)
        player.doubloons += numSabers
        game.printer.print(Channel.Debug, tag + ": +" + numSabers + " Doubloons")
        RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(6, 5, 2, 1, 3, 4)(player.playerId)
    }
}