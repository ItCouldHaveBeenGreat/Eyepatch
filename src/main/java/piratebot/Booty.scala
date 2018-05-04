package main.java.piratebot

import com.rits.cloning.Immutable

@Immutable
object Booty extends Enumeration {
    type Booty = Value
    val Goods, Jewels, Chest, TreasureMap, CursedMask, Saber, SpanishOfficer = Value
}
