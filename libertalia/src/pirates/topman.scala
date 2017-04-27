class Topman(player: Player) extends Pirate(player) {
    val rank = 16
    val name = "Topman"

    override def endOfVoyageAction = {
        // If there are no dens which are explicitly smaller, award bonus
        // NOTE: Allows ties
        val smallerDenCounts = PlayerManager.players.map( p => p.pirates.size )
                                            .filter( count => count < player.pirates.size)
        if (smallerDenCounts.size == 0) {
            player.doubloons += 5
            println(tag + ": +5 Doubloons")
        } else {
            println(tag + ": No bonus")
        }
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(3, 2, 5, 4, 6, 1)(player.playerId);
    }
}