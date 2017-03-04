class Parrot(player: Player) extends Pirate(player) {
    val rank = 1

   override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.PlayPirateFromHand,
                InputManager.getPlayerHandFromPlayer(player))
        if (!request.answered) {
            return RetriableMethodResponse.PendingInput
        }
        // TODO: add to round
        state = PirateState.Discard
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}