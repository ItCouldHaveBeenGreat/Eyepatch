object PlayerManager {
    // TODO: null is evil
    val MAX_PLAYERS = 6
    var players : List[Player] = null
    
    def build(numPlayers: Int) = {
        if (numPlayers > MAX_PLAYERS) {
            throw new IllegalStateException("Maximum players is " + MAX_PLAYERS + ", received " + numPlayers)
        }
        players = List.fill(numPlayers)(new Player())
    }
    
    def getPlayer(playerId: Int) : Player = {
        return players(playerId)
    }
    
    def getLeftPlayer(playerId : Int) : Player = {
        return players((playerId + players.size - 1) % players.size)
    }
    
    def getAdjacentPlayers(playerId : Int) : List[Player] = {
        return List(players((playerId + 1) % players.size),
                    players((playerId + players.size - 1) % players.size))
    }
}