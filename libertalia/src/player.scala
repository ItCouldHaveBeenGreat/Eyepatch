import scala.collection.mutable.ArrayBuffer

class Player(val playerId : Int) {
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
        doEndOfVoyageActions()
        cleanPirates()
        sellBooty()
        doubloons = Math.max(0, doubloons)
        points += doubloons
        OutputManager.print(Channel.Game, "Player " + playerId + " earned " + doubloons + " for a total of " + points + " points")
    }
    
    private def doEndOfVoyageActions() = {
        OutputManager.print(Channel.Debug, "Player " + playerId + " running end of voyage actions")
        pirates.filter( p => p.state == PirateState.Den).foreach( p =>
            p.endOfVoyageAction
        )
    }
    
    private def cleanPirates() = {
        pirates.filter( p => p.state == PirateState.Den || p.state == PirateState.Discard).foreach(
            p => p.state = PirateState.OutOfPlay
        )
    }
    
    private def sellBooty() = {
        // TODO: Is this ineffiecent? Yes.
        OutputManager.print(Channel.Debug, "Player " + playerId + " booty: " + booty)
        val gain = booty.count( b => b == Booty.Goods ) * 1 +
                   booty.count( b => b == Booty.Jewels ) * 3 +
                   booty.count( b => b == Booty.Chest ) * 5 +
                   booty.count( b => b == Booty.CursedMask ) * -3 +
                   booty.count( b => b == Booty.TreasureMap ) / 3 * 12
        
        doubloons += gain
        booty.clear
        OutputManager.print(Channel.Debug, "Player " + playerId + " sold their booty for " + gain + " doubloons")
    }
}