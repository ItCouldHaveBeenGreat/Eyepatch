package libertalia

class Mutineer(player: Player) extends Pirate(player) {
    val rank = 13
    val name = "Mutineer"

    override def nightAction: RetriableMethodResponse.Value = {
        // NOTE: depends on player.pirates being ordered in ascending rank
        val lowestRank = player.pirates.find(p => p.state == PirateState.Den && p != this)
        if (lowestRank != None) {
            val toDiscard = lowestRank.get
            toDiscard.state = PirateState.Discard
            player.doubloons += 2
            OutputManager.print(Channel.Pirate, tag + ": Discarded " + toDiscard.name + "; +2 Doubloons")
        } else {
            OutputManager.print(Channel.Pirate, tag + ": Nobody to discard")
        }
        return RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        return Array(6, 5, 2, 1, 3, 4)(player.playerId);
    }
}