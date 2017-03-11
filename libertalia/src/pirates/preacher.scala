class Preacher(player: Player) extends Pirate(player) {
    val rank = 6
    val name = "Preacher"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        while (player.booty.size > 1) {
            val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.DiscardBooty,
                InputManager.getBootyFromPlayer(player))
            if (!request.answered) {
                return RetriableMethodResponse.PendingInput
            } else {
                val b = InputManager.getBootyFromInput(request)
                InputManager.removeInputRequest(request.playerId)
                player.booty -= b
                println("Player " + player.playerId + " discarded " + b)
            }
        }
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction = {
        player.doubloons += 5
        println(tag + ": +5 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}