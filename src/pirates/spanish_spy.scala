class SpanishSpy(player: Player) extends Pirate(player) {
    val rank = 17
    val name = "SpanishSpy"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        // TOOD: Implement booty drawing
        // TOOD: Implement
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}