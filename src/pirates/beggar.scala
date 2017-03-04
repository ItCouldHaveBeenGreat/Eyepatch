class Beggar(player: Player) extends Pirate(player) {
    val rank = 3
    val name = "Beggar"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons += 3
        // TOOD: Implement sorted round order, ideally with sorted, iterable data structure
        println(tag + ": +3 Doubloons")
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}