class GovernorsDaughter(player: Player) extends Pirate(player) {
    val rank = 25

    override def endOfVoyageAction(): RetriableMethodResponse.Value = {
        if (otherGovernorsDaughter) {
            player.doubloons = Math.max(0, player.doubloons - 3)
            println("Governor's Daughter: -3 Doubloons")
        } else {
            player.doubloons += 6
            println("Governor's Daughter: +6 Doubloons")
        }
        return RetriableMethodResponse.Complete
    }
    
    private def otherGovernorsDaughter : Boolean = {
        for (p <- PlayerManager.instance.players) {
            if (p.pirates(rank).state == PirateState.Den) {
                return true;
            }
        }
        return false;
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}