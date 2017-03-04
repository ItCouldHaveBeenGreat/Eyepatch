class Recruiter(player: Player) extends Pirate(player) {
    val rank = 4

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.RecruitPirateFromDen,
                InputManager.getPlayerDenFromPlayer(player))
        if (!request.answered) {
            return RetriableMethodResponse.PendingInput
        }
        val pirateId = InputManager.getPirateIdFromInput(request)
        player.pirates(pirateId).state = PirateState.Hand
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}
