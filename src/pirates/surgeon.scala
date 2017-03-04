class Surgeon(player: Player) extends Pirate(player) {
    val rank = 22
    val name = "Surgeon"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        // TOOD: Implement
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}