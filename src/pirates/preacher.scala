class Preacher(player: Player) extends Pirate(player) {
    val rank = 6
    val name = "Preacher"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        while (player.booty.size > 1) {
            val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.DiscardBooty,
                InputManager.getPlayerHandFromPlayer(player))
            if (!request.answered) {
                return RetriableMethodResponse.PendingInput
            }
            val b = InputManager.getBootyFromInput(request)
            player.booty -= b
            println("Player " + player.playerId + " discarded " + b)
        }
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction(): RetriableMethodResponse.Value = {
        player.doubloons += 5
        println(tag + ": +5 Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}