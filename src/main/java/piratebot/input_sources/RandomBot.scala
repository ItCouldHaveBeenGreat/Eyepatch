package main.java.piratebot.input_sources

import main.java.piratebot.{Game, InputRequest, Player}

import scala.util.Random

class RandomBot extends InputSource with Statistics {
    override def makeDecision(request: InputRequest, state: Seq[Int], game: Game) : Int = {
        request.choices.values.toList(Random.nextInt(request.choices.values.size))
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = {
        if (player.points == players.maxBy( p => p.points ).points) {
            addCounter("wins", 1)
        }
        addCounter("games", 1)
    }

    override def endSession(): Unit = {
        printCounters()
    }
}