object PlayerManager {
    // TODO: null is evil
    var players : List[Player] = null
    
    def build(numPlayers: Int) = {
        players = List.fill(numPlayers)(new Player())
    }
    
    def getLeftPlayer(playerId : Int) : Player = {
        return players((playerId + players.size - 1) % players.size)
    }
    
    def getAdjacentPlayers(playerId : Int) : List[Player] = {
        return List(players((playerId + 1) % players.size),
                    players((playerId + players.size - 1) % players.size))
    }
}