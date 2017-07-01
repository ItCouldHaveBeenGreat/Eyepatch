package main.java.eyepatch

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Boreal on 7/1/17.
  */
object PlayerManager {
    // TODO: null is evil
    val MAX_PLAYERS = 6
    var players = ArrayBuffer[Player]()

    def build(numPlayers: Int) = {
        if (numPlayers > MAX_PLAYERS) {
            throw new IllegalStateException("Maximum players is " + MAX_PLAYERS + ", received " + numPlayers)
        }
        players.clear()
        for (i <- 0 until numPlayers) {
            players += new Player(i)
        }
    }

    def getPlayer(playerId: Int) : Player = {
        players(playerId)
    }

    def getLeftPlayer(playerId : Int) : Player = {
        players((playerId + players.size - 1) % players.size)
    }

    def getAdjacentPlayers(playerId : Int) : List[Player] = {
        List(players((playerId + 1) % players.size),
                    players((playerId + players.size - 1) % players.size))
    }
}
