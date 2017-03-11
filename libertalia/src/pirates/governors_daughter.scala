class GovernorsDaughter(player: Player) extends Pirate(player) {
    val rank = 25
    val name = "Governor's Daughter"

    override def endOfVoyageAction = {
        if (otherGovernorsDaughter) {
            player.doubloons = Math.max(0, player.doubloons - 3)
            println(tag + ": -3 Doubloons")
        } else {
            player.doubloons += 6
            println(tag + ": +6 Doubloons")
        }
    }
    
    private def otherGovernorsDaughter : Boolean = {
        for (p <- PlayerManager.players) {
            if (p.getPirate(rank).state == PirateState.Den) {
                return true;
            }
        }
        return false;
    }
    
    def getSubRank(player : Player) : Int = {
        return 1; // TODO: Implement
    }
}