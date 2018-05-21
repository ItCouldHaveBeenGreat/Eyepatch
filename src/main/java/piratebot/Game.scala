package main.java.piratebot

import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Game(numPlayers : Int, var totalVoyages: Int = 1) {


    val bootyBag = new BootyBag()
    val printer = new Printer()
    val inputManager = new InputManager(this)
    val playerManager = new PlayerManager(this, numPlayers)
    var currentVoyage: Voyage = new Voyage(this, numPlayers)
    var voyagesTaken : Int = 0

    dealPirates(9)


    def makeProgress() : RetriableMethodResponse.Value = {
        val response = currentVoyage.makeProgress()
        if (response == RetriableMethodResponse.Complete) {
            printer.print(Channel.Debug, "Voyage " + voyagesTaken + " complete")
            voyagesTaken += 1
            if (voyagesTaken >= totalVoyages) {
                printer.print(Channel.Debug, "Game complete")
                endGame()
                RetriableMethodResponse.Complete
            } else {
                dealPirates(6)
                currentVoyage = new Voyage(this, numPlayers)
                RetriableMethodResponse.MadeProgress
            }
        } else {
            // Pass response up if PendingInput or MadeProgress
            return response
        }
    }
    
    private def dealPirates(numPirates : Int): Unit = {
        for (_ <- 1 to numPirates) {
           val deck = playerManager.players.head.pirates.filter( p => p.state == PirateState.Deck)
           val pirateToDraw = deck(Random.nextInt(deck.size)).rank
           for (p <- playerManager.players) {
               p.dealPirate(pirateToDraw)
           }
            printer.print(Channel.Game, "Game dealt pirate " + pirateToDraw)
        }
    }
    
    private def endGame(): Unit = {
        printer.print(Channel.Game, "Final Scores: ")
        for (p <- playerManager.players) {
            printer.print(Channel.Game, "Player " + p.playerId + ": " + p.points + " points")
        }
    }

    // Returns game state as if the player is always player 1, but preserves order
    def getNormalizedGameState(playerId : Int) : Seq[Int] = {
        val gameState = ArrayBuffer[Int]()
        appendCommonState(gameState)
        for (i <- playerId until playerManager.players.size) {
            val player = playerManager.getPlayer(i)
            appendPlayerState(gameState, player)
        }
        for (i <- 0 until playerId) {
            val player = playerManager.getPlayer(i)
            appendPlayerState(gameState, player)
        }
        for (_ <- playerManager.players.size until playerManager.MAX_PLAYERS) {
            appendEmptyPlayer(gameState)
        }
        gameState
    }

    private def appendCommonState(gameState : ArrayBuffer[Int]): Unit = {
        gameState += playerManager.players.size
        gameState += voyagesTaken
        gameState += currentVoyage.roundsTaken
        gameState ++= currentVoyage.bootySets.flatMap(bootySet => bootySet.map(b => b.id).padTo(playerManager.MAX_PLAYERS, -1))
    }

    private def appendPlayerState(gameState : ArrayBuffer[Int], player : Player) : Unit = {
        gameState += player.doubloons // +1
        gameState += player.points // +1
        gameState ++= player.pirates.map( p => p.publicState.id ) // +30
        // Return the quantity of each bootyType in sorted order (not clear what that is?!)
        gameState ++= player.booty.toSeq.sorted.map(entry => entry._2)
    }

    private def appendEmptyPlayer(gameState : ArrayBuffer[Int]): Unit = {
        gameState ++= ArrayBuffer.fill(51)(-1) // TODO: this is wrong
    }

}