package main.java.piratebot.input_sources

import main.java.piratebot.{InputRequest, Player}

import scala.util.Random

class RandomBot extends InputSource {
    override def makeDecision(request: InputRequest, state: Seq[Int]) : Int = {
        return request.choices.values.toList(Random.nextInt(request.choices.values.size))
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = { }

    override def endSession(): Unit = { }
}