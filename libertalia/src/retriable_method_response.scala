package libertalia

object RetriableMethodResponse extends Enumeration {
    type RetriableMethodResponse = Value
    val Complete, MadeProgress, PendingInput = Value
}