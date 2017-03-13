class SpanishGovernor(player: Player) extends Pirate(player) {
    val rank = 30
    val name = "Spanish Governor"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        for (p <- player.pirates) {
            if (p.state == PirateState.Den) {
                p.state = PirateState.Discard
            }
        }
        println(tag + ": Discarded all characters in den")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}