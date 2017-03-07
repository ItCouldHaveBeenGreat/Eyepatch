class Brute(player: Player) extends Pirate(player) {
    val rank = 14
    val name = "Brute"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val pirateToKill = round.pirates.last
        round.killPirate(pirateToKill)
        println(tag + ": killed " + pirateToKill.tag)
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}