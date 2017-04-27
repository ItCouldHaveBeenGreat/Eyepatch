class Cook(player: Player) extends Pirate(player) {
    val rank = 18
    val name = "Cook"

    private var hasClaimedFirstBooty : Boolean = false

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
        val response = claimBooty(round)
        if (response != RetriableMethodResponse.Complete) {
            return response;
        } else {
            hasClaimedFirstBooty = false
            return RetriableMethodResponse.Complete
        }
    }

    def getSubRank(player : Player) : Int = {
        return Array(5, 4, 1, 6, 2, 3)(player.playerId);
    }
}