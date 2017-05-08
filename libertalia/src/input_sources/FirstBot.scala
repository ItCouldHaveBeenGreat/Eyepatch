package input_sources

import libertalia.{InputRequest, Player, PlayerManager}

import scala.util.Random

class FirstBot() extends InputSource with Annotating with Statistics with Networked {
    private val MAKE_DECISION = "make_decision"
    private val NETWORK_ID = "first"
    private val AGENT = "first"

    override def makeDecision(request: InputRequest, state: Seq[Int]): String = {
        val input = "[" + request.playerId + "," + request.inputType.id + "," + state.mkString(",") + "]"
        val choices = "[" + request.validAnswers.mkString(",") + "]"
        val get_response = get(MAKE_DECISION, Map("network_id" -> NETWORK_ID, "input" -> input, "choices" -> choices))
        val choice = get_response.substring(1, get_response.size - 1)
        record(request.playerId, AGENT, request.inputType, 1, state)

        if(request.validAnswers.contains(choice)) {
            addCounter("legal moves", 1)
            return choice
        }
        addCounter("moves", 1)

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