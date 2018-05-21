package main.java.piratebot.pirates

import main.java.piratebot._

class Merchant(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 21
    val name = "Merchant"

    // call me dumb, but I'm starting to prefer nulls...
    var bootyTypeToSell: Option[Booty.Value] = None

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        // determine which type of booty to sell, then determine how many
        if (bootyTypeToSell.isEmpty) {
            // I kind of hate how this is using the input manager logic... should break out into booty util
            val sellableBooty = game.inputManager.getSellableBootyTypesFromPlayer(player)
            if (sellableBooty.isEmpty) {
                game.printer.print(Channel.Debug, tag + ": Has nothing to sell")
                return RetriableMethodResponse.Complete
            }
            else if (sellableBooty.size == 1) {
                bootyTypeToSell = Some(sellableBooty.head)
            } else {
                val request = game.inputManager.postAndGetInputRequest(
                    player.playerId,
                    InputRequestType.SellBooty,
                    game.inputManager.getSellableBootyTypesFromPlayer(player)
                        .map(bootyType => bootyType.toString -> bootyType.id)
                        .toMap)
                if (request.answer.isEmpty) {
                    return RetriableMethodResponse.PendingInput
                } else {
                    bootyTypeToSell = Some(game.inputManager.getBootyFromInput(request))
                    game.inputManager.removeInputRequest(request.playerId)
                }
            }
        }
        if (bootyTypeToSell.isDefined) {
            if (player.booty(bootyTypeToSell.get) >= 3) {
                val request = game.inputManager.postAndGetInputRequest(
                    player.playerId,
                    InputRequestType.SellThreeBooty,
                    game.inputManager.getBooleanAnswers)
                if (request.answer.isEmpty) {
                    return RetriableMethodResponse.PendingInput
                } else {
                    if (game.inputManager.getBooleanResponseFromInput(request)) {
                        game.bootyBag.putBack(bootyTypeToSell.get, 3)
                        player.booty(bootyTypeToSell.get) -= 3
                        player.doubloons += 5
                        game.printer.print(Channel.Debug, tag + ": Sold 3 " + bootyTypeToSell.get + " for 5 doubloons")
                    } else {
                        game.bootyBag.putBack(bootyTypeToSell.get, 2)
                        player.booty(bootyTypeToSell.get) -= 2
                        player.doubloons += 3
                        game.printer.print(Channel.Debug, tag + ": Sold 2 " + bootyTypeToSell.get + " for 3 doubloons")
                    }
                    game.inputManager.removeInputRequest(request.playerId)
                }
            } else {
                // sell two booty
                // stupid repeated code stupid stupid
                game.bootyBag.putBack(bootyTypeToSell.get, 2)
                player.booty(bootyTypeToSell.get) -= 2
                player.doubloons += 3
                game.printer.print(Channel.Debug, tag + ": Sold 2 " + bootyTypeToSell.get + " for 3 doubloons")
            }
        }
        bootyTypeToSell = None
        RetriableMethodResponse.Complete
    }

    def getSubRank(player : Player) : Int = {
        Array(1, 6, 3, 2, 4, 5)(player.playerId)
    }
}