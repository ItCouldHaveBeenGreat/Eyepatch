class Parrot(player: Player) extends Pirate(player) {
    val rank = 1
    val name = "Parrot"

   override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.PlayPirateFromHand,
                InputManager.getPlayerHandFromPlayer(player))
        if (!request.answered) {
            return RetriableMethodResponse.PendingInput
        }
        val pirateRank = InputManager.getPirateIdFromInput(request)
        InputManager.removeInputRequest(request.playerId)
        
        val pirateToAdd = PlayerManager.players(request.playerId).getPirate(pirateRank)
        round.addPirate(pirateToAdd)
        round.killPirate(this)

        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}