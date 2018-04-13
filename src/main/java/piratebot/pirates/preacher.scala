package libertalia

import main._
import main.java.piratebot._

class Preacher(player: Player) extends Pirate(player) {
    val rank = 6
    val name = "Preacher"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        if (player.booty.values.sum <= 1) {
            return RetriableMethodResponse.Complete
        } else {
            val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.DiscardAllButOneBooty,
                InputManager.getBootyTypesOwnedByPlayer(player)
                    .map(bootyType => bootyType.id.toString))
            if (!request.answered) {
                return RetriableMethodResponse.PendingInput
            } else {
                val bootyTypeToKeep = InputManager.getBootyFromInput(request)
                player.booty.keys.foreach(bootyType => player.booty(bootyType) = 0)
                player.booty(bootyTypeToKeep) = 1
                OutputManager.print(Channel.Pirate, "Player " + player.playerId + " kept one " + bootyTypeToKeep)
                InputManager.removeInputRequest(request.playerId)
                return RetriableMethodResponse.Complete
            }
        }
    }

    override def endOfVoyageAction = {
        player.doubloons += 5
        OutputManager.print(Channel.Pirate, tag + ": +5 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(1, 6, 3, 2, 4, 5)(player.playerId);
    }
}