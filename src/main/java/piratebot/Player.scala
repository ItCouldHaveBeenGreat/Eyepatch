package main.java.piratebot

import libertalia._
import main.java.piratebot.pirates._
import org.slf4j.LoggerFactory

import scala.collection.mutable

class Player(val game: Game, val playerId : Int) {
    var points : Int = 0
    var doubloons : Int = 0
    val booty = mutable.HashMap(
        Booty.Chest -> 0,
        Booty.CursedMask -> 0,
        Booty.Goods -> 0,
        Booty.Jewels -> 0,
        Booty.Saber -> 0,
        Booty.SpanishOfficer -> 0,
        Booty.TreasureMap -> 0)
    val pirates : Seq[Pirate] = List( // NOTE: Order matters
        new Parrot(game, this),
        new Monkey(game, this),
        new Beggar(game, this),
        new Recruiter(game, this),
        new CabinBoy(game, this),
        new Preacher(game, this),
        new Barkeep(game, this),
        new Waitress(game, this),
        new Carpenter(game, this),
        new FrenchOfficer(game, this),
        new VoodooWitch(game, this),
        new FreedSlave(game, this),
        new Mutineer(game, this),
        new Brute(game, this),
        new Gunner(game, this),
        new Topman(game, this),
        new SpanishSpy(game, this),
        new Cook(game, this),
        new Bosun(game, this),
        new Armorer(game, this),
        new Merchant(game, this),
        new Surgeon(game, this),
        new Treasurer(game, this),
        new Gambler(game, this),
        new GovernorsDaughter(game, this),
        new Quartermaster(game, this),
        new GrannyWata(game, this),
        new FirstMate(game, this),
        new Captain(game, this),
        new SpanishGovernor(game, this))

    def getPirate(rank : Int) : Pirate = {
        // Rank is 1 indexed
        pirates(rank - 1)
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
        game.printer.print(Channel.Game, "Player " + playerId + " earned " + doubloons + " for a total of " + points + " points")
    }

    private def doEndOfVoyageActions(): Unit = {
        game.printer.print(Channel.Debug, "Player " + playerId + " running end of voyage actions")
        pirates.filter( p => p.state == PirateState.Den).foreach( p =>
            p.endOfVoyageAction()
        )
    }

    private def cleanPirates(): Unit = {
        pirates.filter( p => p.state == PirateState.Den || p.state == PirateState.Discard).foreach(
            p => p.state = PirateState.OutOfPlay
        )
    }

    private def sellBooty(): Unit = {
        game.printer.print(Channel.Debug, "Player " + playerId + " booty: " + booty)
        val gain = booty(Booty.Goods) * 1 +
                   booty(Booty.Jewels) * 3 +
                   booty(Booty.Chest) * 5 +
                   booty(Booty.CursedMask) * -3 +
                   booty(Booty.TreasureMap) / 3 * 12

        doubloons += gain
        booty.keys.foreach(bootyType => booty(bootyType) = 0)
        game.printer.print(Channel.Debug, "Player " + playerId + " sold their booty for " + gain + " doubloons")
    }
}