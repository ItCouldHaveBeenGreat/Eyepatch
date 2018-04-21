package main.java.piratebot.input_sources

import main.java.piratebot.{InputRequest, Player}

import scala.util.Random

class WinTrainingRandomBot(consumer: TrainingDataConsumer) extends InputSource with Annotating with Statistics {

    override def makeDecision(request: InputRequest, state: Seq[Int]) : Int = {
        val decision = request.choices.values.toList(Random.nextInt(request.choices.values.size))
        record(request.playerId, "random", request.inputType, decision.toInt, state)
        decision
    }

    override def endGame(player: Player, players: Seq[Player]) = {
        // only upload data if we won
        if (player.points == players.maxBy( p => p.points ).points) {
            consumer.consume(getData)
            addCounter("wins", 1)
        }
        addCounter("games", 1)
        clearData
    }

    override def endSession() = {
        printCounters
    }
}
