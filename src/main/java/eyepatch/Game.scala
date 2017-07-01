package main.java.eyepatch

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

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
        appendCommonState(gameState)
        for (player <- PlayerManager.players) {
            appendPlayerState(gameState, player)
        }
        for (i <- PlayerManager.players.size to PlayerManager.MAX_PLAYERS - 1) {
            appendEmptyPlayer(gameState)
        }
        return gameState
    }

    // Returns game state as if the player is always player 1, but preserves order
    def getNormalizedGameState(playerId : Int) : Seq[Int] = {
        val gameState = ArrayBuffer[Int]()
        appendCommonState(gameState)
        for (i <- playerId to PlayerManager.players.size - 1) {
            val player = PlayerManager.getPlayer(i)
            appendPlayerState(gameState, player)
        }
        for (i <- 0 to playerId - 1) {
            val player = PlayerManager.getPlayer(i)
            appendPlayerState(gameState, player)
        }
        for (i <- PlayerManager.players.size to PlayerManager.MAX_PLAYERS - 1) {
            appendEmptyPlayer(gameState)
        }
        return gameState
    }

    def appendCommonState(gameState : ArrayBuffer[Int]) = {
        gameState += PlayerManager.players.size
        gameState += voyagesTaken
        gameState += currentVoyage.roundsTaken
        gameState ++= currentVoyage.bootySets.map( bootySet => bootySet.map( b => b.id ).padTo(PlayerManager.MAX_PLAYERS, -1)).flatten
    }

    def appendPlayerState(gameState : ArrayBuffer[Int], player : Player) = {
        gameState += player.doubloons
        gameState += player.points
        gameState ++= player.pirates.map( p => p.publicState.id )
        // The maximum number of items you can have is the total number of Cursed Masks (10) + Cook (2) + Recruiter (1)
        // + Cook (2) + Surgeon (1) + Cook (2) + 1, or 19
        gameState ++= player.booty.map( b => b.id ).padTo(19, -1)
    }

    def appendEmptyPlayer(gameState : ArrayBuffer[Int]): Unit = {
        gameState ++= ArrayBuffer.fill(51)(-1)
    }

}