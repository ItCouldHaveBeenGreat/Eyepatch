package main.java.eyepatch.input_sources

import main.java.eyepatch.{InputRequest, Player}

import scala.util.Random

class RandomBot extends InputSource {
    override def makeDecision(request: InputRequest, state: Seq[Int]) : String = {
        return request.validAnswers(Random.nextInt(request.validAnswers.size))
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = { }

    override def endSession(): Unit = { }
}