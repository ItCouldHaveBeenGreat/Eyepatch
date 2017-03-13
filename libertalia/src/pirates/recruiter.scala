class Recruiter(player: Player) extends Pirate(player) {
    val rank = 4
    val name = "Recruiter"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        if (player.pirates.count ( p => p.state == PirateState.Den) == 0) {
            println(tag + ": Nobody to recruit")
            return RetriableMethodResponse.Complete
        }
        val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.RecruitPirateFromDen,
                InputManager.getPlayerDenFromPlayer(player))
        if (!request.answered) {
            return RetriableMethodResponse.PendingInput
        }
        val pirateId = InputManager.getPirateIdFromInput(request)
        InputManager.removeInputRequest(request.playerId)
        player.getPirate(pirateId).state = PirateState.Hand
        println(tag + ": Recruits " + player.getPirate(pirateId).name)
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}