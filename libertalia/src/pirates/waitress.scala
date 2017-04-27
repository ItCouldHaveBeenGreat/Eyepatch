class Waitress(player: Player) extends Pirate(player) {
    val rank = 8
    val name = "Waitress"

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
                println(tag + ": Sold Map for +3 Doubloons")
            }
            InputManager.removeInputRequest(request.playerId)
        }
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(2, 1, 4, 3, 5, 6)(player.playerId);
    }
}