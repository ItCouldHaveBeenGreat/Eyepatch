package main.java.piratebot.pirates

import main.java.piratebot._

class Recruiter(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 4
    val name = "Recruiter"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        if (player.pirates.count ( p => p.state == PirateState.Den) == 0) {
            game.printer.print(Channel.Debug, tag + ": Nobody to recruit")
            return RetriableMethodResponse.Complete
        }
        val request = game.inputManager.postOrGetInputRequest(
                player.playerId,
                InputRequestType.RecruitPirateFromDen,
            game.inputManager.getPlayerDenFromPlayer(player))
        if (request.answer.isEmpty) {
            return RetriableMethodResponse.PendingInput
        }
        val pirateId = game.inputManager.getPirateIdFromInput(request)
        game.inputManager.removeInputRequest(request.playerId)
        player.getPirate(pirateId).state = PirateState.Hand
        game.printer.print(Channel.Debug, tag + ": Recruits " + player.getPirate(pirateId).name)
        RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        Array(5, 4, 1, 6, 2, 3)(player.playerId)
    }
}
