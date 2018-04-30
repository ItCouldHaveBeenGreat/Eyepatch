package main.java.piratebot.pirates

import main.java.piratebot._

class Preacher(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 6
    val name = "Preacher"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        if (player.booty.values.sum <= 1) {
            return RetriableMethodResponse.Complete
        } else {
            val request = game.inputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.DiscardAllButOneBooty,
                game.inputManager.getBootyTypesOwnedByPlayer(player)
                    .map(bootyType => bootyType.toString -> bootyType.id)
                    .toMap)
            if (request.answer.isEmpty) {
                return RetriableMethodResponse.PendingInput
            } else {
                val bootyTypeToKeep = game.inputManager.getBootyFromInput(request)
                player.booty.keys.foreach(bootyType => player.booty(bootyType) = 0)
                player.booty(bootyTypeToKeep) = 1
                logger.debug("Player " + player.playerId + " kept one " + bootyTypeToKeep)
                game.inputManager.removeInputRequest(request.playerId)
                return RetriableMethodResponse.Complete
            }
        }
    }

    override def endOfVoyageAction(): Unit = {
        player.doubloons += 5
        logger.debug(tag + ": +5 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        Array(1, 6, 3, 2, 4, 5)(player.playerId)
    }
}