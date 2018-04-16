package libertalia

import main._
import main.java.piratebot._

class Merchant(player: Player) extends Pirate(player) {
    val rank = 21
    val name = "Merchant"

    // call me dumb, but I'm starting to prefer nulls...
    var bootyTypeToSell: Option[Booty.Value] = None

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        // determine which type of booty to sell, then determine how many
        if (bootyTypeToSell.isEmpty) {
            // I kind of hate how this is using the input manager logic... should break out into booty util
            val sellableBooty = InputManager.getSellableBootyTypesFromPlayer(player)
            if (sellableBooty.isEmpty) {
                OutputManager.print(Channel.Pirate, tag + ": Has nothing to sell")
                return RetriableMethodResponse.Complete
            }
            else if (sellableBooty.size == 1) {
                bootyTypeToSell = Some(sellableBooty.head)
            } else {
                val request = InputManager.postAndGetInputRequest(
                    player.playerId,
                    InputRequestType.SellBooty,
                    InputManager.getSellableBootyTypesFromPlayer(player)
                        .map(bootyType => bootyType.toString -> bootyType.id)
                        .toMap)
                if (request.answer.isEmpty) {
                    return RetriableMethodResponse.PendingInput
                } else {
                    bootyTypeToSell = Some(InputManager.getBootyFromInput(request))
                    InputManager.removeInputRequest(request.playerId)
                }
            }
        }
        if (bootyTypeToSell.isDefined) {
            if (player.booty(bootyTypeToSell.get) >= 3) {
                val request = InputManager.postAndGetInputRequest(
                    player.playerId,
                    InputRequestType.SellThreeBooty,
                    InputManager.getBooleanAnswers)
                if (request.answer.isEmpty) {
                    return RetriableMethodResponse.PendingInput
                } else {
                    if (InputManager.getBooleanResponseFromInput(request)) {
                        player.booty(bootyTypeToSell.get) -= 3
                        player.doubloons += 5
                        OutputManager.print(Channel.Pirate, tag + ": Sold 3 " + bootyTypeToSell.get + " for 5 doubloons")
                    } else {
                        player.booty(bootyTypeToSell.get) -= 2
                        player.doubloons += 3
                        OutputManager.print(Channel.Pirate, tag + ": Sold 2 " + bootyTypeToSell.get + " for 3 doubloons")
                    }
                    InputManager.removeInputRequest(request.playerId)
                }
            } else {
                // sell two booty
                // stupid repeated code stupid stupid
                player.booty(bootyTypeToSell.get) -= 2
                player.doubloons += 3
                OutputManager.print(Channel.Pirate, tag + ": Sold 2 " + bootyTypeToSell.get + " for 3 doubloons")
            }
        }
        bootyTypeToSell = None
        RetriableMethodResponse.Complete
    }

    def getSubRank(player : Player) : Int = {
        Array(1, 6, 3, 2, 4, 5)(player.playerId)
    }
}