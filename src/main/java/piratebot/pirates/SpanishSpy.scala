package main.java.piratebot.pirates

import main.java.piratebot._

class SpanishSpy(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 17
    val name = "SpanishSpy"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        for (_ <- 1 to player.booty(Booty.SpanishOfficer)) {
            val bootyDrawn = game.bootyBag.draw
            player.booty(bootyDrawn) += 1
            game.printer.print(Channel.Debug, tag + ": Drew a " + bootyDrawn)
        }
        player.booty(Booty.SpanishOfficer) = 0
        
        RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(4, 3, 6, 5, 1, 2)(player.playerId)
    }
}