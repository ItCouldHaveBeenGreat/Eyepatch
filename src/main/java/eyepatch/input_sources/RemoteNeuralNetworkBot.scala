package main.java.eyepatch.input_sources

import main.java.eyepatch.{Channel, InputRequest, OutputManager, Player}

import scala.util.Random

class RemoteNeuralNetworkBot(val modelKey : String) extends InputSource with Annotating with Statistics with Networked {
    private val MAKE_DECISION = "make_decision"
    private val CREATE_OR_LOAD_NETWORK = "create_network"

    // ensure network is created
    post(CREATE_OR_LOAD_NETWORK, Map("network_id" -> modelKey))

    override def makeDecision(request: InputRequest, state: Seq[Int]): String = {
        val input = "[" + request.playerId + "," + request.inputType.id + "," + state.mkString(",") + "]"
        val choices = "[" + request.validAnswers.mkString(",") + "]"
        val get_response = get(MAKE_DECISION, Map("network_id" -> modelKey, "input" -> input, "choices" -> choices))
        val choice = get_response.substring(1, get_response.size - 1)
        record(request.playerId, modelKey, request.inputType, 1, state)

        if(request.validAnswers.contains(choice)) {
            addCounter("legal moves", 1)
            addCounter("moves", 1)
            return choice
        }
        addCounter("moves", 1)
        OutputManager.print(Channel.Game, "Illegal move for " + modelKey + ": " + choice + " for request[ "
                            + request.playerId + ", " + request.inputType + ", " + request.validAnswers + "]")


        return request.validAnswers(Random.nextInt(request.validAnswers.size)).toString
    }

    override def endGame(player: Player, players: Seq[Player]) = {
        if (player.points == players.maxBy( p => p.points ).points) {
            addCounter("wins", 1)
        }
        addCounter("games", 1)
        clearData
    }

    override def endSession() = {
        printCounters()
    }
}