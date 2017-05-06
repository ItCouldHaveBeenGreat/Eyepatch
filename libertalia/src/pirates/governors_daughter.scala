class GovernorsDaughter(player: Player) extends Pirate(player) {
    val rank = 25
    val name = "Governor's Daughter"

    override def endOfVoyageAction = {
        if (otherGovernorsDaughter) {
            player.doubloons -= 3
            OutputManager.print(Channel.Pirate, tag + ": -3 Doubloons")
        } else {
            player.doubloons += 6
            OutputManager.print(Channel.Pirate, tag + ": +6 Doubloons")
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
        return Array(4, 3, 6, 5, 1, 2)(player.playerId);
    }
}