class Merchant(player: Player) extends Pirate(player) {
    val rank = 21
    val name = "Merchant"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        // TOOD: Implement
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}