class FrenchOfficer(player: Player) extends Pirate(player) {
    val rank = 10

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        if (player.doubloons <= 9) {
            println("French Officer: +5 Doubloons")
            player.doubloons += 5
        } else {
            println("French Officer: did nothing")
        }
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}