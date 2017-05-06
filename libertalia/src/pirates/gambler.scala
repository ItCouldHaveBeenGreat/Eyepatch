package libertalia

class Gambler(player: Player) extends Pirate(player) {
    val rank = 24
    val name = "Gambler"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        player.doubloons -= player.booty.size
        OutputManager.print(Channel.Pirate, tag + ": -" + player.booty.size + " Doubloons")
        return RetriableMethodResponse.Complete
    }

    override def endOfVoyageAction = {
        player.doubloons += 8
        OutputManager.print(Channel.Pirate, tag + ": +8 Doubloons")
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(4, 3, 6, 5, 1, 2)(player.playerId);
    }
}