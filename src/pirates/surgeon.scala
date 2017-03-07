class Surgeon(player: Player) extends Pirate(player) {
    val rank = 22
    val name = "Surgeon"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.RevivePirateFromDiscard,
                InputManager.getPlayerDiscardFromPlayer(player))
        if (!request.answered) {
            return RetriableMethodResponse.PendingInput
        }
        val pirateRank = InputManager.getPirateIdFromInput(request)
        InputManager.removeInputRequest(request.playerId)
        
        val pirateToRevive = PlayerManager.players(request.playerId).getPirate(pirateRank)
        pirateToRevive.state = PirateState.Hand
        pirateToRevive.visible = false

        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}