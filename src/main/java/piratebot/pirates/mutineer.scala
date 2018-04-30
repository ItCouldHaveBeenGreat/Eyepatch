package main.java.piratebot.pirates

import main.java.piratebot._

class Mutineer(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 13
    val name = "Mutineer"

    override def nightAction: RetriableMethodResponse.Value = {
        // NOTE: depends on player.main.pirates being ordered in ascending rank
        val lowestRank = player.pirates.find(p => p.state == PirateState.Den && p != this)
        if (lowestRank.isDefined) {
            val toDiscard = lowestRank.get
            toDiscard.state = PirateState.Discard
            player.doubloons += 2
            logger.debug(tag + ": Discarded " + toDiscard.name + "; +2 Doubloons")
        } else {
            logger.debug(tag + ": Nobody to discard")
        }
        RetriableMethodResponse.Complete
    }
    
    def getSubRank(player : Player) : Int = {
        Array(6, 5, 2, 1, 3, 4)(player.playerId)
    }
}