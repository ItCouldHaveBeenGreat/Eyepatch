class Bosun(player: Player) extends Pirate(player) {
    val rank = 19
    val name = "Bosun"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val numLowRank = player.pirates.count( p => p.state == PirateState.Den && p.rank < rank)
        player.doubloons += 2 * numLowRank
        println(tag + ": +" + numLowRank * 2 + " Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}