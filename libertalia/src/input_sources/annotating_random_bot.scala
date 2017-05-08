package input_sources

import libertalia._

import scala.util.Random

class AnnotatingRandomBot() extends InputSource with Annotating with Networked with Statistics {
    val TRAIN_NETWORK = "train_network"
    val CREATE_NETWORK = "create_network"
    val network_id = "first"
    val agent = "randombot"
    
    override def makeDecision(request: InputRequest, state: Seq[Int]) : String = {
        val decision = request.validAnswers(Random.nextInt(request.validAnswers.size))
        record(request.playerId, agent, request.inputType, decision.toInt, state)
        return decision
    }

    def uploadDecisions(annotations : Map[String, String]) = {
        try {
            annotate(annotations)
            post(CREATE_NETWORK, Map("network_id" -> network_id))
            post(TRAIN_NETWORK, Map("network_id" -> network_id, "data" -> getAnnotatedData))
        } catch {
            case e: Exception => println(e)
        }
    }

    override def endGame(player: Player, players: Seq[Player]) = {
        // only upload data if we won
        if (player.points == players.maxBy( p => p.points ).points) {
            uploadDecisions(Map[String, String]())
            addCounter("wins", 1)
        }
        addCounter("games", 1)
        clearData
    }

    override def endSession() = {
        printCounters()
    }
}