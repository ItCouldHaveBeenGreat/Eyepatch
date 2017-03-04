class Monkey(player: Player) extends Pirate(player) {
    val rank = 2
    val name = "Monkey"

   override def dayAction(round : Round): RetriableMethodResponse.Value = {
        val leftPlayer = PlayerManager.getLeftPlayer(player.playerId)
        val numMasks = player.booty.count(b => b == Booty.CursedMask)
        for (i <- 1 to numMasks) {
            leftPlayer.booty += Booty.CursedMask
            // TODO: There must be a better way to batch remove
            player.booty -= Booty.CursedMask
        }
        println(tag + ": transferred all Cursed Masks to player " + leftPlayer.playerId)
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}