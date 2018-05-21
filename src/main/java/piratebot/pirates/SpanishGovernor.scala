package main.java.piratebot.pirates

import main.java.piratebot._

class SpanishGovernor(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 30
    val name = "Spanish Governor"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        for (p <- player.pirates) {
            if (p.state == PirateState.Den) {
                p.state = PirateState.Discard
            }
        }
        game.printer.print(Channel.Debug, tag + ": Discarded all characters in den")
        RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
         Array(3, 2, 5, 4, 6, 1)(player.playerId)
    }
}