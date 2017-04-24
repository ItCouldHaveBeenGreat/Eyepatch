class VoodooWitch(player: Player) extends Pirate(player) {
    val rank = 11
    val name = "Voodoo Witch"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val numDiscard = player.pirates.count( p => p.state == PirateState.Discard )
        player.doubloons += 2 * numDiscard
        println(tag + ": +" + numDiscard * 2 + " Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}