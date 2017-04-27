class FrenchOfficer(player: Player) extends Pirate(player) {
    val rank = 10
    val name = "French Officer"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        if (player.doubloons <= 9) {
            println(tag + ": +5 Doubloons")
            player.doubloons += 5
        } else {
            println(tag + ": did nothing")
        }
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(4, 3, 6, 5, 1, 2)(player.playerId);
    }
}