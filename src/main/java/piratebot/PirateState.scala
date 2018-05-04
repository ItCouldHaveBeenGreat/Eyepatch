package main.java.piratebot

import com.rits.cloning.Immutable

@Immutable
object PirateState extends Enumeration {
    type PirateState = Value
    val Board, Den, Deck, Discard, Hand, OutOfPlay = Value
}

@Immutable
object PublicPirateState extends Enumeration {
    type PublicPirateState = Value
    val Board, Den, Discard, Hand, OutOfPlay, Unknown = Value
}