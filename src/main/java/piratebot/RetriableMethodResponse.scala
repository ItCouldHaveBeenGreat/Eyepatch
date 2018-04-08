package main.java.piratebot

/**
  * Created by Boreal on 7/1/17.
  */
object RetriableMethodResponse extends Enumeration {
    type RetriableMethodResponse = Value
    val Complete, MadeProgress, PendingInput = Value
}
