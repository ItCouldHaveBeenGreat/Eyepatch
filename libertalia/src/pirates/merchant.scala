class Merchant(player: Player) extends Pirate(player) {
    val rank = 21
    val name = "Merchant"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        // TOOD: Implement
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(1, 6, 3, 2, 4, 5)(player.playerId);
    }
}