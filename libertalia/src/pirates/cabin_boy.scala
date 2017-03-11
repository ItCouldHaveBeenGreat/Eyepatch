class CabinBoy(player: Player) extends Pirate(player) {
    val rank = 5
    val name = "Cabin Boy"

    override def duskAction(round : Round): RetriableMethodResponse.Value = {
        println(tag + ": Doesn't claim booty")
        return RetriableMethodResponse.Complete
    }

    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}