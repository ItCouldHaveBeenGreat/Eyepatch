package main.java.piratebot.pirates

import main.java.piratebot._

class Gunner(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 15
    val name = "Gunner"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        // NOTE: According to the rules, you MUST use the Gunner's effect, regardless of your inability of to pay or the total lack of targets
        // NOTE: Yeah, it's kind of silly
        val doubloonsToCharge = Math.min(3, player.doubloons)
        player.doubloons -= doubloonsToCharge
        game.printer.print(Channel.Debug, tag + ": -" + doubloonsToCharge + " doubloons")

        if (game.inputManager.getAllDenPirates(player).nonEmpty) {
            val request = game.inputManager.postOrGetInputRequest(
                player.playerId,
                InputRequestType.KillPirateInAnyDen,
                game.inputManager.getAllDenPirates(player))
            if (request.answer.isEmpty) {
                return RetriableMethodResponse.PendingInput
            } else {
                val target = game.inputManager.getTargetPirateFromInput(request)
                target.state = PirateState.Discard
                game.printer.print(Channel.Debug, tag + ": killed " + target.tag)
                game.inputManager.removeInputRequest(request.playerId)
            }
        }

        RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(2, 1, 4, 3, 5, 6)(player.playerId)
    }
}