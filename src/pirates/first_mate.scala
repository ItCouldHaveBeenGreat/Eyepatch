class FirstMate(player: Player) extends Pirate(player) {
    val rank = 28
    val name = "First Mate"

    override def endOfVoyageAction(): RetriableMethodResponse.Value = {
        val numDen = player.pirates.count( p => p.state == PirateState.Den)
        player.doubloons += numDen
        println(tag + ": +" + numDen + " Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}