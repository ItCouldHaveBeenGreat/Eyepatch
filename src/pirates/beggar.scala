class Beggar(player: Player) extends Pirate(player) {
    val rank = 3
    val name = "Beggar"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val firstPlayer = round.pirates.last.player
        val doubloonsToTake = Math.max(3, firstPlayer.doubloons)
        player.doubloons += doubloonsToTake
        firstPlayer.doubloons -= doubloonsToTake
        println(tag + ": stole " + doubloonsToTake + " from " + firstPlayer.playerId)

        println(tag + ": +3 Doubloons")
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}