package main.java.piratebot.input_sources
import main.java.piratebot.{InputRequest, Player}

import scala.util.Random

class BranchingNeuralNetworkBot(key: String) extends InputSource with Statistics {
    override def makeDecision(request: InputRequest, state: Seq[Int]): String = {
        // currently a random bot
        val decision = request.validAnswers(Random.nextInt(request.validAnswers.size))
        return decision
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = {

    }

    override def endSession(): Unit = {

    }
}
