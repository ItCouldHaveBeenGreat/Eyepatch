package libertalia

import main.java.piratebot._

class GrannyWata(game: Game, player: Player) extends Pirate(game, player) {
    val rank = 27
    val name = "Granny Wata"

    override def nightAction: RetriableMethodResponse.Value = {
        val grannyWatas = getAllGrannyWatasInDen
        if (grannyWatas.size > 1) {
            grannyWatas.foreach(gw => gw.state = PirateState.Discard)
            logger.debug(tag + ": All " + name + " discarded")
        } else {
            player.doubloons += 2
            logger.debug(tag + ": +2 Doubloons")
        }
        RetriableMethodResponse.Complete
    }
    
    private def getAllGrannyWatasInDen: Seq[Pirate] = {
        game.playerManager.players.map( p => p.getPirate(rank))
                                    .filter( p => p.state == PirateState.Den)
    }
    
    def getSubRank(player : Player) : Int = {
        Array(6, 5, 2, 1, 3, 4)(player.playerId)
    }
}