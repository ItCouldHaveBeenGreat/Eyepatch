class Quartermaster(player: Player) extends Pirate(player) {
    val rank = 26
    val name = "Quartermaster"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons += player.booty.size
        println(tag + ": +" + player.booty.size + " Doubloons")
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction = {
        player.doubloons = Math.max(0, player.doubloons - 8)
        println(tag + ": -8 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(5, 4, 1, 6, 2, 3)(player.playerId);
    }
}