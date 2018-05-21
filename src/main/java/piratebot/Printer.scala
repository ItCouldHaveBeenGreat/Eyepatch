package main.java.piratebot

import com.rits.cloning.Immutable

import scala.collection.mutable

class Printer {
    val channels = mutable.Map(
        Channel.Pirate -> true,
        Channel.Debug -> true,
        Channel.Game -> true,
        Channel.Input -> true)

    def print(channel: Channel.Value, message: String): Unit = {
        if (channels(channel)) {
            println(channel.toString + ": " + message)
        }
    }

    def silence(): Unit = {
        channels.keys.foreach(key => channels(key) = false)
    }
}

@Immutable
object Channel extends Enumeration {
    type Channel = Value
    val Pirate,
        Debug,
        Game,
        Input = Value
}