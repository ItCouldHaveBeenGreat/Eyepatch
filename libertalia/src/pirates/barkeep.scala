package libertalia

class Barkeep(player: Player) extends Pirate(player) {
    val rank = 7
    val name = "Barkeep"

    override def nightAction: RetriableMethodResponse.Value = {
        player.doubloons += 1
        OutputManager.print(Channel.Pirate, tag + ": +1 Doubloons")
        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(1, 6, 3, 2, 4, 5)(player.playerId);
    }
}