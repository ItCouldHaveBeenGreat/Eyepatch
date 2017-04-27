class Armorer(player: Player) extends Pirate(player) {
    val rank = 20
    val name = "Armorer"

    override def nightAction: RetriableMethodResponse.Value = {
        val numSabers = player.booty.count(b => b == Booty.Saber)
        player.doubloons += numSabers
        println(tag + ": +" + numSabers + " Doubloons")
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(6, 5, 2, 1, 3, 4)(player.playerId);
    }
}