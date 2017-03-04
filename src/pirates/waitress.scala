class Waitress(player: Player) extends Pirate(player) {
    val rank = 8

    override def nightAction: RetriableMethodResponse.Value = {
        if (player.booty.contains(Booty.TreasureMap)) {
            val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.SellMap,
                InputManager.getBooleanAnswers)
            if (!request.answered) {
                return RetriableMethodResponse.PendingInput
            }
            if (InputManager.getBooleanResponseFromInput(request)) {
                player.booty -= Booty.TreasureMap
                player.doubloons += 3
                println("Waitress: Sold Map for +3 Doubloons")
            }
        }
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}