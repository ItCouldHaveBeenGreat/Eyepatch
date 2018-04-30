package main.java.piratebot.pirates

import main.java.piratebot._

class Waitress(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 8
    val name = "Waitress"

    override def nightAction: RetriableMethodResponse.Value = {
        if (player.booty(Booty.TreasureMap) > 0) {
            val request = game.inputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.SellMap,
                game.inputManager.getBooleanAnswers)
            if (request.answer.isEmpty) {
                return RetriableMethodResponse.PendingInput
            }

            if (game.inputManager.getBooleanResponseFromInput(request)) {
                player.booty(Booty.TreasureMap) -= 1
                player.doubloons += 3
                logger.debug(tag + ": Sold Map for +3 Doubloons")
            }
            game.inputManager.removeInputRequest(request.playerId)
        }
        RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(2, 1, 4, 3, 5, 6)(player.playerId)
    }
}