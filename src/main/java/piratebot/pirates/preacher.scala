package main.java.piratebot.pirates

import main.java.piratebot._

class Preacher(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 6
    val name = "Preacher"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        if (player.booty.values.sum <= 1) {
            RetriableMethodResponse.Complete
        } else {
            val request = game.inputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.DiscardAllButOneBooty,
                game.inputManager.getBootyTypesOwnedByPlayer(player)
                    .map(bootyType => bootyType.toString -> bootyType.id)
                    .toMap)
            if (request.answer.isEmpty) {
                RetriableMethodResponse.PendingInput
            } else {
                val bootyTypeToKeep = game.inputManager.getBootyFromInput(request)
                player.booty.keys.foreach { bootyType =>
                    if (bootyType.equals(bootyTypeToKeep)) {
                        game.bootyBag.putBack(bootyType, player.booty(bootyType) - 1)
                        player.booty(bootyTypeToKeep) = 1
                    } else {
                        game.bootyBag.putBack(bootyType, player.booty(bootyType))
                        player.booty(bootyType) = 0
                    }
                }
                game.printer.print(Channel.Debug, "Player " + player.playerId + " kept one " + bootyTypeToKeep)
                game.inputManager.removeInputRequest(request.playerId)
                RetriableMethodResponse.Complete
            }
        }
    }

    override def endOfVoyageAction(): Unit = {
        player.doubloons += 5
        game.printer.print(Channel.Debug, tag + ": +5 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        Array(1, 6, 3, 2, 4, 5)(player.playerId)
    }
}