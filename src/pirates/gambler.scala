class Gambler(player: Player) extends Pirate(player) {
    val rank = 24
    val name = "Gambler"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons -= player.booty.size
        println(tag + ": -" + player.booty.size + " Doubloons")
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction(): RetriableMethodResponse.Value = {
        player.doubloons += 8
        println(tag + ": +8 Doubloons")
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}