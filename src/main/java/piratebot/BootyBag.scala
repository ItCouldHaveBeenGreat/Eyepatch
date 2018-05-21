package main.java.piratebot

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class BootyBag {
    private val bootyBag = ArrayBuffer[Booty.Value]()

    def draw: Booty.Value = {
        val drawn = bootyBag(Random.nextInt(bootyBag.size))
        bootyBag -= drawn
        drawn
    }

    def putBack(bootyType: Booty.Value, number: Int = 1): Unit = {
        0.until(number).foreach {
            bootyBag += bootyType
        }
    }

    def build: Unit = {
        bootyBag.clear
        for (_ <- 1 to 4) { bootyBag += Booty.Chest }
        for (_ <- 1 to 6) { bootyBag += Booty.Jewels }
        for (_ <- 1 to 10) { bootyBag += Booty.Goods }
        for (_ <- 1 to 6) { bootyBag += Booty.SpanishOfficer }
        for (_ <- 1 to 6) { bootyBag += Booty.Saber }
        for (_ <- 1 to 8) { bootyBag += Booty.TreasureMap }
        for (_ <- 1 to 10) { bootyBag += Booty.CursedMask }
    }
}
