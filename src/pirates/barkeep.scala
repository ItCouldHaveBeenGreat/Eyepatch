class Barkeep(player: Player) extends Pirate(player) {
    val rank = 7

    override def nightAction: RetriableMethodResponse.Value = {
        player.doubloons += 1
        println("Barkeep: +1 Doubloons")
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}