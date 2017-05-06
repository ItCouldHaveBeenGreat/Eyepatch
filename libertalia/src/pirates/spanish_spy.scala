package libertalia

class SpanishSpy(player: Player) extends Pirate(player) {
    val rank = 17
    val name = "SpanishSpy"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val numSpanishOfficers = player.booty.count( b => b == Booty.SpanishOfficer )
        for (i <- 1 to numSpanishOfficers) {
            player.booty -= Booty.SpanishOfficer
            val bootyDrawn = BootyBag.draw
            player.booty += bootyDrawn
            OutputManager.print(Channel.Pirate, tag + ": Drew a " + bootyDrawn)
        }
        
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(4, 3, 6, 5, 1, 2)(player.playerId);
    }
}