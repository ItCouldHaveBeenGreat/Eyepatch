class Carpenter(player: Player) extends Pirate(player) {
    val rank = 9

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons = Math.ceil(player.doubloons.toFloat * 0.5)
        println("Pirate: -50% Doubloons")
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction(): RetriableMethodResponse.Value = {
        player.doubloons += 10
        println("Carpenter: +10 Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}