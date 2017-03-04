class Captain(player: Player) extends Pirate(player) {
    val rank = 29
    val name = "Captain"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons += 3
        println(tag + ": +3 Doubloons")
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction: RetriableMethodResponse.Value = {
        val numCursedRelics = player.booty.count(b => b == Booty.CursedMask)
        player.doubloons = Math.max(0, player.doubloons - numCursedRelics * 3)
        println(tag + ": -" + numCursedRelics * 3 + " Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}