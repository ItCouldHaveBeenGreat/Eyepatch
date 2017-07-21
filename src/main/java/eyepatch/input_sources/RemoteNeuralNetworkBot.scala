package main.java.eyepatch.input_sources

import main.java.eyepatch.{Channel, InputRequest, OutputManager, Player}

import scala.util.Random

class RemoteNeuralNetworkBot(val modelKey : String) extends InputSource with Statistics with Networked {
    private val MAKE_DECISION = "make_decision"
    private val CREATE_OR_LOAD_NETWORK = "create_network"
    private val NETWORK_LAYER_SIZES = "[694, 694]"
    private val NETWORK_INPUT_SIZE = 347;
    private val NETWORK_OUTPUT_SIZE = 1;
    private val NETWORK_CLASSES = {
      "[" + (List.range(0, 31)
        ++ List.range(101, 131)
        ++ List.range(201, 231)
        ++ List.range(301, 331)
        ++ List.range(401, 431)
        ++ List.range(501, 531)
      ).mkString(",") + "]"


    }
    // ensure network is created
    post(CREATE_OR_LOAD_NETWORK, Map("network_id" -> modelKey,
                                     "layer_sizes" -> NETWORK_LAYER_SIZES,
                                     "output_classes" -> NETWORK_CLASSES,
                                     "input_size" -> NETWORK_INPUT_SIZE.toString,
                                     "output_size" -> NETWORK_OUTPUT_SIZE.toString))

    override def makeDecision(request: InputRequest, state: Seq[Int]): String = {
        val input = "[" + request.playerId + "," + request.inputType.id + "," + state.mkString(",") + "]"
        val choices = "[" + request.validAnswers.mkString(",") + "]"
        val get_response = get(MAKE_DECISION, Map("network_id" -> modelKey,
                                                  "input" -> input,
                                                  "choices" -> choices))
        val choice = get_response.substring(1, get_response.size - 1)

        if(request.validAnswers.contains(choice)) {
            addCounter("legal moves", 1)
            addCounter("moves", 1)
            return choice
        }
        addCounter("moves", 1)
        OutputManager.print(Channel.Bot, "Illegal move for " + modelKey + ": " + choice + " for request[ "
                            + request.playerId + ", " + request.inputType + ", " + request.validAnswers + "]")


        return request.validAnswers(Random.nextInt(request.validAnswers.size)).toString
    }

    override def endGame(player: Player, players: Seq[Player]) = {
        if (player.points == players.maxBy( p => p.points ).points) {
            addCounter("wins", 1)
        }
        addCounter("games", 1)
    }

    override def endSession() = {
        printCounters()
    }
}