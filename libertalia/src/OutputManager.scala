package libertalia

import scala.collection.mutable

/**
  * Created by Spook on 5/4/2017.
  */
object OutputManager {

  private val enabledChannels = mutable.HashSet[Channel.Value]()

  def enableChannel(channel : Channel.Value) = {
    enabledChannels += channel
  }

  def disableChannel(channel : Channel.Value) = {
    enabledChannels -= channel
  }

  def print(channel : Channel.Value, output : String) = {
    if (enabledChannels.contains(channel)) {
      println(output)
    }
  }
}

object Channel extends Enumeration {
  type PublicPirateState = Value
  val Pirate, Game, Debug, Bot, Runner = Value
}
