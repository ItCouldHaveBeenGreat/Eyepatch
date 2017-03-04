import scala.collection.mutable.ArrayBuffer

class Player() {
    val playerId : Int = PlayerIdGenerator.getId()

    var points : Int = 0;
    var doubloons : Int = 0;
    val booty : ArrayBuffer[Booty.Value] = ArrayBuffer()
    val pirates : Seq[Pirate] = List( // NOTE: Order matters
        new Parrot(this),
        new Monkey(this),
        new Beggar(this),
        new Recruiter(this),
        new CabinBoy(this),
        new Barkeep(this),
        new Carpenter(this),
        new FrenchOfficer(this),
        new Cook(this),
        new Armorer(this),
        new GovernorsDaughter(this),
        new Captain(this),
        new SpanishGovernor(this))
    
    
    def startVoyage() = {
        doubloons = 10
    }
    
    def dealPirate(pirateId : Int) = {
        pirates(pirateId).state = PirateState.Hand
    }
    
    def endVoyage() = {
        // enact all den pirate's end of voyage actions
        // clean the hand, den, and graveyard
        
        sellBooty()
        points += doubloons
    }
    
    private def sellBooty() = {
        
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