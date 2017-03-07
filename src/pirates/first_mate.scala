class FirstMate(player: Player) extends Pirate(player) {
    val rank = 28
    val name = "First Mate"

    override def endOfVoyageAction = {
        val numDen = player.pirates.count( p => p.state == PirateState.Den)
        player.doubloons += numDen
        println(tag + ": +" + numDen + " Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}