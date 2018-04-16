package libertalia

import main._
import main.java.piratebot._

class Parrot(player: Player) extends Pirate(player) {
    val rank = 1
    val name = "Parrot"

   override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.PlayPirateFromHand,
                InputManager.getPlayerHandFromPlayer(player))
        if (request.answer.isEmpty) {
            return RetriableMethodResponse.PendingInput
        }
        val pirateRank = InputManager.getPirateIdFromInput(request)
        InputManager.removeInputRequest(request.playerId)
        
        val pirateToAdd = PlayerManager.players(request.playerId).getPirate(pirateRank)
        round.addPirate(pirateToAdd)
        round.killPirate(this)
        OutputManager.print(Channel.Pirate, tag + ": was replaced with " + pirateToAdd.tag)

        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(2, 1, 4, 3, 5, 6)(player.playerId);
    }
}