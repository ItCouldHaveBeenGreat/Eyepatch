package libertalia

object PirateState extends Enumeration {
    type PirateState = Value
    val Board, Den, Deck, Discard, Hand, OutOfPlay = Value
}

object PublicPirateState extends Enumeration {
    type PublicPirateState = Value
    val Board, Den, Discard, Hand, OutOfPlay, Unknown = Value
}