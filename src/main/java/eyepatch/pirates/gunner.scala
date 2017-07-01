package libertalia

import main._
import main.java.eyepatch._

class Gunner(player: Player) extends Pirate(player) {
    val rank = 15
    val name = "Gunner"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        // NOTE: According to the rules, you MUST use the Gunner's effect, regardless of your inability of to pay or the total lack of targets
        // NOTE: Yeah, it's kind of silly
        val doubloonsToCharge = Math.min(3, player.doubloons)
        player.doubloons -= doubloonsToCharge
        OutputManager.print(Channel.Pirate, tag + ": -" + doubloonsToCharge + " doubloons")

        if (InputManager.getAllDenPirates.size > 0) {
            val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.KillPirateInAnyDen,
                InputManager.getAllDenPirates)
            if (!request.answered) {
                return RetriableMethodResponse.PendingInput
            } else {
                val target = InputManager.getTargetPirateFromInput(request)
                target.state = PirateState.Discard
                OutputManager.print(Channel.Pirate, tag + ": killed " + target.tag)
                InputManager.removeInputRequest(request.playerId)
            }
        }

        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(2, 1, 4, 3, 5, 6)(player.playerId);
    }
}