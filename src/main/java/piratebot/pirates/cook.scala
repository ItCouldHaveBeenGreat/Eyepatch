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
            game.printer.print(Channel.Debug, tag + "(1): " + response)
            if (response == RetriableMethodResponse.Complete) {
                hasClaimedFirstBooty = true
            } else {
                return response
            }
        }

        // claim the second booty
        val response = claimBooty(round)
        game.printer.print(Channel.Debug, tag + "(2): " + response)
        if (response != RetriableMethodResponse.Complete) {
            response
        } else {
            hasClaimedFirstBooty = false
            RetriableMethodResponse.Complete
        }
    }

    def getSubRank(player : Player) : Int = {
        Array(5, 4, 1, 6, 2, 3)(player.playerId)
    }
}