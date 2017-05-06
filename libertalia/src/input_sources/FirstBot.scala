package input_sources

import libertalia.{InputRequest, Player}

class FirstBot() extends InputSource with Annotating with Statistics with Networked {
    private val MAKE_DECISION = "make_decision"
    private val NETWORK_ID = "first"
    private val AGENT = "first"

    override def makeDecision(request: InputRequest, state: Seq[Int]): String = {
        val input = "[" + request.playerId.toString + ", " + request.inputType.toString + ", " + state.mkString(", ") + "]"
        val get_response = get(MAKE_DECISION, Map("network_id" -> NETWORK_ID, "input" -> "input"))
        println(get_response)
        record(request.playerId, AGENT, request.inputType, 1, state)

        return "Wo"
    }

    override def endGame(player: Player, players: Seq[Player]) = {

    }

    override def endSession() = {

    }
}