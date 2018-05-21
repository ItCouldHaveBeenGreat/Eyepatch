package main.java.piratebot.pirates

import main.java.piratebot._

class Parrot(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 1
    val name = "Parrot"

   override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val request = game.inputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.PlayPirateFromHand,
                game.inputManager.getPlayerHandFromPlayer(player))
        if (request.answer.isEmpty) {
            return RetriableMethodResponse.PendingInput
        }
        val pirateRank = game.inputManager.getPirateIdFromInput(request)
        game.inputManager.removeInputRequest(request.playerId)
        
        val pirateToAdd = game.playerManager.players(request.playerId).getPirate(pirateRank)
        round.addPirate(pirateToAdd)
        round.killPirate(this)
       game.printer.print(Channel.Debug, tag + ": was replaced with " + pirateToAdd.tag)

       RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(2, 1, 4, 3, 5, 6)(player.playerId)
    }
}