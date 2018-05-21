package main.java.piratebot.input_sources

import main.java.piratebot.{Game, InputRequest, Player}

import scala.io.StdIn

class StandardInputBot(val name: String) extends InputSource with Statistics {
    override def makeDecision(request: InputRequest, state: Seq[Int], game: Game) : Int = {
        var inputChoice = -1
        // add playerId -> color mapping
        while (!request.choices.values.toList.contains(inputChoice)) {
            println("Request for player " + request.playerId + ", " + getPlayerColor(request.playerId) + " to choose " + request.inputType.toString +
                " with choices " + request.choices.toString())
            inputChoice = StdIn.readInt()
        }
        inputChoice
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = {
        if (player.points == players.maxBy( p => p.points ).points) {
            addCounter("wins", 1)
        }
        addCounter("games", 1)
    }

    override def endSession(): Unit = {
        println("Counters for " + name + ":")
        printCounters()
    }
}