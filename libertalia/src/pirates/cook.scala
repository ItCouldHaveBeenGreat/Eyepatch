class Cook(player: Player) extends Pirate(player) {
    val rank = 18
    val name = "Cook"

    private var hasClaimedFirstBooty : Boolean = false

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        // Reset the booty claimed counter in preparation for the dusk action
        hasClaimedFirstBooty = false
        return RetriableMethodResponse.Complete
    }
    override def duskAction(round : Round): RetriableMethodResponse.Value = {
        // claim the first booty
        if (!hasClaimedFirstBooty) {
            val response = claimBooty(round)
            if (response == RetriableMethodResponse.Complete) {
                hasClaimedFirstBooty = true
            } else {
                return response
            }
        }
        // claim the second booty
        return claimBooty(round)
    }

    def getSubRank(player : Player) : Int = {
        return Array(5, 4, 1, 6, 2, 3)(player.playerId);
    }
}