package main.java.piratebot.pirates

import main.java.piratebot._

class Surgeon(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 22
    val name = "Surgeon"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        if (game.inputManager.getPlayerDiscardFromPlayer(player).isEmpty) {
            game.printer.print(Channel.Debug, tag + ": No one to revive")
            return RetriableMethodResponse.Complete
        }
        
        val request = game.inputManager.postOrGetInputRequest(
                player.playerId,
                InputRequestType.RevivePirateFromDiscard,
                game.inputManager.getPlayerDiscardFromPlayer(player))
        if (request.answer.isEmpty) {
            return RetriableMethodResponse.PendingInput
        }
        val pirateRank = game.inputManager.getPirateIdFromInput(request)
        game.inputManager.removeInputRequest(request.playerId)
        
        val pirateToRevive = game.playerManager.players(request.playerId).getPirate(pirateRank)
        pirateToRevive.state = PirateState.Hand
        
        if (player.pirates.count( p => p.state == PirateState.Discard ) > 0) {
            // if there is a pirate remaining in the graveyard,
            // the entire graveyard and the revived pirate become unknown
            pirateToRevive.known = false
            for (p <- player.pirates) {
                if (p.state == PirateState.Discard) {
                    p.known = false
                }
            }
        } else {
            // if the graveyard is emptied, the player's hand returns to a fully known state
            player.pirates.filter( p => p.state == PirateState.Hand ).foreach (p =>
                p.known = true
            )
        }

        RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(2, 1, 4, 3, 5, 6)(player.playerId)
    }
}