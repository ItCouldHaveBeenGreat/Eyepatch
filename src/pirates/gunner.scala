class Gunner(player: Player) extends Pirate(player) {
    val rank = 15
    val name = "Gunner"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        // TOOD: Implement sorted round order, ideally with sorted, iterable data structure
        // TOOD: Implement
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}