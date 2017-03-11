class SpanishSpy(player: Player) extends Pirate(player) {
    val rank = 17
    val name = "SpanishSpy"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val numSpanishOfficers = player.booty.count( b => b == Booty.SpanishOfficer )
        for (i <- 1 to numSpanishOfficers) {
            player.booty -= Booty.SpanishOfficer
            val bootyDrawn = BootyBag.draw
            player.booty += bootyDrawn
            println(tag + ": Drew a " + bootyDrawn)
        }
        
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}