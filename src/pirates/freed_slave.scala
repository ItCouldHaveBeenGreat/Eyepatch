class FreedSlave(player: Player) extends Pirate(player) {
    val rank = 12
    val name = "Freed Slave"

    override def nightAction: RetriableMethodResponse.Value = {
        val numHighRank = player.pirates.count( p => p.state == PirateState.Den && p.rank > rank)
        player.doubloons += numHighRank
        println(tag + ": +" + numHighRank + " Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}