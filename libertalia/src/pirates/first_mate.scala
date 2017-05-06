package libertalia

class FirstMate(player: Player) extends Pirate(player) {
    val rank = 28
    val name = "First Mate"

    override def endOfVoyageAction = {
        val numDen = player.pirates.count( p => p.state == PirateState.Den)
        player.doubloons += numDen
        OutputManager.print(Channel.Pirate, tag + ": +" + numDen + " Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(1, 6, 3, 2, 4, 5)(player.playerId);
    }
}