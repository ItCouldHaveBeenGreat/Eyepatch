class Player() {
    val playerId : Int = PlayerIdGenerator.getId()

    var points : Int = 0;
    var doubloons : Int = 0;
    val booty : Seq[Booty.Value] = List()
    val pirates : Seq[Pirate] = List(
        new GovernorsDaughter(this),
        new Captain(this))
    
    def startVoyage() = {
        doubloons = 10
    }
    
    def dealPirate(pirateId : Int) = {
        pirates(pirateId).state = PirateState.Hand
    }
    
    def endVoyage() = {
        // enact all den pirate's end of voyage actions
        // clean the hand, den, and graveyard
        points += doubloons
    }
}

object PlayerIdGenerator {
    // TODO: This is so bad
    var lastId : Int = -1
    def getId() : Int = {
        lastId += 1
        return lastId
    } 
}