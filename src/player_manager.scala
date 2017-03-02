class PlayerManager(val numPlayers: Int) {
    val players : List[Player] = List.fill(numPlayers)(new Player())
}

object PlayerManager {
    // TODO: null is evil
    private var holder : PlayerManager = null
    
    def instance : PlayerManager = {
        return holder
    }
    
    def build(numPlayers: Int) = {
        holder = new PlayerManager(numPlayers)
    }
}