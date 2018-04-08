package main.java.piratebot.input_sources

import main.java.piratebot.{Channel, InputRequest, OutputManager}

import scala.util.Random

/**
  * Created by Boreal on 7/20/17.
  */
class AnnotatingMutatingBot(network_to_train : String, val mutation_factor : Double) extends AnnotatingRandomBot(network_to_train) {
    private val MAKE_DECISION = "make_decision"

    override def makeDecision(request: InputRequest, state: Seq[Int]) : String = {
        // Make a modeled move
        if (Random.nextDouble() > mutation_factor) {
          val input = "[" + request.playerId + "," + request.inputType.id + "," + state.mkString(",") + "]"
          val choices = "[" + request.validAnswers.mkString(",") + "]"
          val get_response = get(MAKE_DECISION, Map("network_id" -> network_to_train,
            "input" -> input,
            "choices" -> choices))
          val decision = get_response.substring(1, get_response.size - 1)

          if (request.validAnswers.contains(decision)) {
            record(request.playerId, agent, request.inputType, decision.toInt, state)
            return decision
          } else {
            OutputManager.print(Channel.Bot, "Illegal move for " + network_to_train + ": " + decision + " for request[ "
              + request.playerId + ", " + request.inputType + ", " + request.validAnswers + "]")
          }
        }

        // Make a random move
        val decision = request.validAnswers(Random.nextInt(request.validAnswers.size))
        record(request.playerId, agent, request.inputType, decision.toInt, state)
        return decision

    }
}
