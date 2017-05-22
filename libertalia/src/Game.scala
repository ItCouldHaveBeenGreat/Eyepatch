package libertalia

import scala.util.Random
import scala.collection.mutable.ArrayBuffer

class Game(numPlayers : Int) {
    PlayerManager.build(numPlayers)
    
    var currentVoyage: Voyage = new Voyage(numPlayers)
    var voyagesTaken : Int = 0
    val totalVoyages : Int = 3

    dealPirates(9)
    
    def makeProgress() : RetriableMethodResponse.Value = {
        var response = currentVoyage.makeProgress()
        if (response == RetriableMethodResponse.Complete) {
            OutputManager.print(Channel.Game, "Voyage " + voyagesTaken + " complete")
            voyagesTaken += 1
            if (voyagesTaken >= totalVoyages) {
                OutputManager.print(Channel.Game, "Game complete")
                endGame
                return RetriableMethodResponse.Complete
            } else {
                dealPirates(6)
                currentVoyage = new Voyage(numPlayers)
                return RetriableMethodResponse.MadeProgress
            }
        } else {
            // Pass response up if PendingInput or MadeProgress
            return response
        }
    }
    
    def dealPirates(numPirates : Int) = {
        for (i <- 1 to numPirates) {
           val deck = PlayerManager.players.head.pirates.filter( p => p.state == PirateState.Deck)
           val pirateToDraw = deck(Random.nextInt(deck.size)).rank
           for (p <- PlayerManager.players) {
               // NOTE: Rank is 1 indexed
               p.dealPirate(pirateToDraw)
           }
            OutputManager.print(Channel.Game, "Game dealt pirate " + pirateToDraw)
        }
    }
    
    def endGame = {
        OutputManager.print(Channel.Game, "Final Scores: ")
        for (p <- PlayerManager.players) {
            OutputManager.print(Channel.Game, "Player " + p.playerId + ": " + p.points + " points")
        }
    }
    
    def getGameState : Seq[Int] = {
        val gameState = ArrayBuffer[Int]()
        gameState += PlayerManager.players.size
        gameState += voyagesTaken
        gameState += currentVoyage.roundsTaken
        gameState ++= currentVoyage.bootySets.map( bootySet => bootySet.map( b => b.id ).padTo(PlayerManager.MAX_PLAYERS, -1)).flatten
        for (player <- PlayerManager.players) {
            gameState += player.doubloons
            gameState += player.points
            gameState ++= player.pirates.map( p => p.publicState.id )
            // The maximum number of items you can have is the total number of Cursed Masks (10) + Cook (2) + Recruiter (1) 
            // + Cook (2) + Surgeon (1) + Cook (2) + 1, or 19
            gameState ++= player.booty.map( b => b.id ).padTo(19, -1)
        }
        for (i <- PlayerManager.players.size to PlayerManager.MAX_PLAYERS - 1) {
            gameState ++= ArrayBuffer.fill(51)(-1)
        }
        return gameState
    }
}