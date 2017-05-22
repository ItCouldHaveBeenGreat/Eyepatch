package input_sources

import libertalia.{InputRequest, Player}

import scala.util.Random

class RandomBot extends InputSource {
    override def makeDecision(request: InputRequest, state: Seq[Int]) : String = {
        return request.validAnswers(Random.nextInt(request.validAnswers.size))
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = ???

    override def endSession(): Unit = ???
}