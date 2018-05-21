package main.java.piratebot

import scala.collection.mutable.ArrayBuffer

class Voyage(val game: Game, val numPlayers : Int) {
    val totalRounds: Int = 6
    
    game.bootyBag.build
    val bootySets: ArrayBuffer[ArrayBuffer[Booty.Value]] = ArrayBuffer.fill(totalRounds)( ArrayBuffer.fill(numPlayers)( game.bootyBag.draw ) )
    var currentRound = new Round(game, bootySets(0))
    var roundsTaken : Int = 0

    game.playerManager.players.foreach( p => p.doubloons = 10)
    
    def makeProgress() : RetriableMethodResponse.Value = {
        val response = currentRound.makeProgress()
        if (response == RetriableMethodResponse.Complete) {
            game.printer.print(Channel.Debug, "Round " + roundsTaken + " complete")
            roundsTaken += 1
            if (roundsTaken >= totalRounds) {
                endVoyage()
                RetriableMethodResponse.Complete
            } else {
                currentRound = new Round(game, bootySets(roundsTaken))
                RetriableMethodResponse.MadeProgress
            }
        } else {
            // Pass response up if PendingInput or MadeProgress
            response
        }
    }
    
    def endVoyage(): Unit = {
        game.playerManager.players.foreach ( p => p.endVoyage() )
    }
}