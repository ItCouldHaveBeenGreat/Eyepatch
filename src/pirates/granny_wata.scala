class GrannyWata(player: Player) extends Pirate(player) {
    val rank = 27
    val name = "Granny Wata"

    override def nightAction(): RetriableMethodResponse.Value = {
        val grannyWatas = getAllGrannyWatasInDen
        if (grannyWatas.size > 1) {
            grannyWatas.foreach(gw => gw.state = PirateState.Discard)
            println(tag + ": All " + name + " discarded")
        } else {
            player.doubloons += 2
            println(tag + ": +2 Doubloons")
        }
        return RetriableMethodResponse.Complete
    }
    
    private def getAllGrannyWatasInDen : List[Pirate] = {
        return PlayerManager.players.map( p => p.pirates(rank))
                                    .filter( p => p.state == PirateState.Den)
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}