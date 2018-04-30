package libertalia

import main.java.piratebot._

class Cook(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 18
    val name = "Cook"

    private var hasClaimedFirstBooty : Boolean = false

    override def duskAction(round : Round): RetriableMethodResponse.Value = {
        // claim the first booty
        if (!hasClaimedFirstBooty) {
            val response = claimBooty(round)
            logger.debug(tag + "(1): " + response)
            if (response == RetriableMethodResponse.Complete) {
                hasClaimedFirstBooty = true
            } else {
                return response
            }
        }

        // claim the second booty
        val response = claimBooty(round)
        logger.debug(tag + "(2): " + response)
        if (response != RetriableMethodResponse.Complete) {
            return response
        } else {
            hasClaimedFirstBooty = false
            return RetriableMethodResponse.Complete
        }
    }

    def getSubRank(player : Player) : Int = {
        Array(5, 4, 1, 6, 2, 3)(player.playerId)
    }
}