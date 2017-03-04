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
        new Preacher(this),
        new Barkeep(this),
        new Waitress(this),
        new Carpenter(this),
        new FrenchOfficer(this),
        new VoodooWitch(this),
        new FreedSlave(this),
        new Mutineer(this),
        new Brute(this),
        new Gunner(this),
        new Topman(this),
        new SpanishSpy(this),
        new Cook(this),
        new Bosun(this),
        new Armorer(this),
        new Merchant(this),
        new Surgeon(this),
        new Treasurer(this),
        new Gambler(this),
        new GovernorsDaughter(this),
        new Quartermaster(this),
        new GrannyWata(this),
        new FirstMate(this),
        new Captain(this),
        new SpanishGovernor(this))
    
    def getPirate(rank : Int) : Pirate = {
        // Rank is 1 indexed
        return pirates(rank - 1)
    }
    
    def dealPirate(rank : Int) = {
        getPirate(rank).state = PirateState.Hand
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