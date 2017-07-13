package main.java.eyepatch

import scala.collection.mutable.ArrayBuffer

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

    /**
      * Returns a player's playerId as seen from the perspective of the localPlayer
      * Assumes players are arranged in a circle, with the first player being left of the second and right of the last
      * e.g: player.playerId = 3, localPlayer.playerId = 2, result = 1
      * e.g: player.playerId = 1, localPlayer.playerId = 2, result = 5
      * @param player
      * @param localPlayer
      * @return
      */
    def getLocalPlayerId(localPlayer : Player, player : Player) : Int = {
        return (player.playerId - localPlayer.playerId + players.size) % players.size
    }

    def getPlayerFromLocalPlayerId(localPlayer : Player, playerId : Int) : Player = {
        return getPlayer((playerId + localPlayer.playerId) % players.size)
    }

    /**
      * Returns the player to the left of the given playerId
      * @param player
      * @return
      */
    def getLeftPlayer(player : Player) : Player = {
        players((player.playerId + players.size - 1) % players.size)
    }

    /**
      * Returns the players to the right and left (in that order) of the given player
      * @param player
      * @return
      */
    def getAdjacentPlayers(player : Player) : List[Player] = {
        List(players((player.playerId + 1) % players.size),
                    players((player.playerId + players.size - 1) % players.size))
    }
}
