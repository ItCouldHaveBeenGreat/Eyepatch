class Captain(player: Player) extends Pirate(player) {
    val rank = 29
    val name = "Captain"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons += 3
        println(tag + ": +3 Doubloons")
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction = {
        val numCursedRelics = player.booty.count(b => b == Booty.CursedMask)
        player.doubloons -= numCursedRelics * 3
        println(tag + ": -" + numCursedRelics * 3 + " Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(2, 1, 4, 3, 5, 6)(player.playerId);
    }
}