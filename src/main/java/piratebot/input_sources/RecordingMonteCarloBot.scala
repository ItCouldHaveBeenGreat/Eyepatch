package main.java.piratebot.input_sources

import main.java.piratebot._

class RecordingMonteCarloBot(name: String, thinkingTimeInMilliseconds: Int = 5000) extends MoveRecordingInputSource(name + ".data") with Statistics {

    override def makeRecordedDecision(request: InputRequest, state: Seq[Int], game: Game) : Int = {
        //logger.info("Beginning rollout for " + request.inputType.toString + ", " + request.playerId)
        val searchTree = new MonteCarloSearchTree(game, request.playerId, request.choices.values.toList, request.inputType)
        searchTree.playOut(request.playerId, thinkingTimeInMilliseconds)
        println(name + " did " + searchTree.rootNode.plays + " playouts")
        searchTree.getBestChoice
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = {
        if (player.points == players.maxBy( p => p.points ).points) {
            addCounter("wins", 1)
        }
        addCounter("games", 1)
    }

    override def endSession(): Unit = {
        printCounters(name)
    }
}

