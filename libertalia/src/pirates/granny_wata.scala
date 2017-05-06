class GrannyWata(player: Player) extends Pirate(player) {
    val rank = 27
    val name = "Granny Wata"

    override def nightAction(): RetriableMethodResponse.Value = {
        val grannyWatas = getAllGrannyWatasInDen()
        if (grannyWatas.size > 1) {
            grannyWatas.foreach(gw => gw.state = PirateState.Discard)
            OutputManager.print(Channel.Pirate, tag + ": All " + name + " discarded")
        } else {
            player.doubloons += 2
            OutputManager.print(Channel.Pirate, tag + ": +2 Doubloons")
        }
        return RetriableMethodResponse.Complete
    }
    
    private def getAllGrannyWatasInDen() : Seq[Pirate] = {
        return PlayerManager.players.map( p => p.getPirate(rank))
                                    .filter( p => p.state == PirateState.Den)
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(6, 5, 2, 1, 3, 4)(player.playerId);
    }
}