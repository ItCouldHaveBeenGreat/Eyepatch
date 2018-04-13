package main.java.piratebot

import libertalia._

import scala.collection.mutable

class Player(val playerId : Int) {
    var points : Int = 0;
    var doubloons : Int = 0;
    val booty = mutable.HashMap(
        Booty.Chest -> 0,
        Booty.CursedMask -> 0,
        Booty.Goods -> 0,
        Booty.Jewels -> 0,
        Booty.Saber -> 0,
        Booty.SpanishOfficer -> 0,
        Booty.TreasureMap -> 0)
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

    def dealPirate(rank : Int): Unit = {
        getPirate(rank).state = PirateState.Hand
    }

    def endVoyage(): Unit = {
        // enact all den pirate's end of voyage actions
        // clean the hand, den, and graveyard
        doEndOfVoyageActions()
        cleanPirates()
        sellBooty()
        doubloons = Math.max(0, doubloons)
        points += doubloons
        OutputManager.print(Channel.Game, "Player " + playerId + " earned " + doubloons + " for a total of " + points + " points")
    }

    private def doEndOfVoyageActions(): Unit = {
        OutputManager.print(Channel.Debug, "Player " + playerId + " running end of voyage actions")
        pirates.filter( p => p.state == PirateState.Den).foreach( p =>
            p.endOfVoyageAction
        )
    }

    private def cleanPirates(): Unit = {
        pirates.filter( p => p.state == PirateState.Den || p.state == PirateState.Discard).foreach(
            p => p.state = PirateState.OutOfPlay
        )
    }

    private def sellBooty(): Unit = {
        OutputManager.print(Channel.Debug, "Player " + playerId + " booty: " + booty)
        val gain = booty(Booty.Goods) * 1 +
                   booty(Booty.Jewels) * 3 +
                   booty(Booty.Chest) * 5 +
                   booty(Booty.CursedMask) * -3 +
                   booty(Booty.TreasureMap) / 3 * 12

        doubloons += gain
        booty.keys.foreach(bootyType => booty(bootyType) = 0)
        OutputManager.print(Channel.Debug, "Player " + playerId + " sold their booty for " + gain + " doubloons")
    }
}