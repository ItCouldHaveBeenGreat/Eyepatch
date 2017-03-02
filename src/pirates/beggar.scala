class Beggar(player: Player) extends Pirate(player) {
    val rank = 3

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons += 3
        println("Beggar: +3 Doubloons")
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}