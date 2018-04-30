package main.java.piratebot

class PlayerManager(game: Game, val numPlayers: Int) {
    var players: Seq[Player] = (0 until numPlayers).map(player_id => new Player(game, player_id))

    val MAX_PLAYERS = 6

    def getPlayer(playerId: Int) : Player = {
        players(playerId)
    }

    /**
      * Returns a player's playerId as seen from the perspective of the localPlayer
      * Assumes players are arranged in a circle, with the first player being left of the second and right of the last
      * e.g: player.playerId = 3, localPlayer.playerId = 2, result = 1
      * e.g: player.playerId = 1, localPlayer.playerId = 2, result = 5
      */
    def getLocalPlayerId(localPlayer : Player, player : Player) : Int = {
        (player.playerId - localPlayer.playerId + players.size) % players.size
    }

    def getPlayerFromLocalPlayerId(localPlayer : Player, playerId : Int) : Player = {
        getPlayer((playerId + localPlayer.playerId) % players.size)
    }

    /**
      * Returns the player to the left of the given playerId
      */
    def getLeftPlayer(player : Player) : Player = {
        players((player.playerId + players.size - 1) % players.size)
    }

    /**
      * Returns the players to the right and left (in that order) of the given player
      */
    def getAdjacentPlayers(player : Player) : List[Player] = {
        List(players((player.playerId + 1) % players.size), players((player.playerId + players.size - 1) % players.size))
    }
}
