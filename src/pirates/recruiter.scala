class Recruiter(player: Player) extends Pirate(player) {
    val rank = 4
    val name = "Recruiter"

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
        println(tag + ": Recruits " + player.pirates(pirateId).name)
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}
