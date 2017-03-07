class Treasurer(player: Player) extends Pirate(player) {
    val rank = 23
    val name = "Treasurer"

    override def endOfVoyageAction = {
        val numCommodities = player.booty.count(b => b == Booty.Goods ||
                                                     b == Booty.Jewels ||
                                                     b == Booty.Chest)
        player.doubloons += numCommodities
        println(tag + ": +" + numCommodities + " Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}