package main.java.piratebot

import input_sources.{RandomBot, _}
import org.slf4j.LoggerFactory

import scala.util.Random

object Runner {
    private val logger = LoggerFactory.getLogger(getClass)

    def runGame(players : List[InputSource]): Unit = {
        val numPlayers = players.size
        val game = new Game(numPlayers)

        while (game.makeProgress() != RetriableMethodResponse.Complete) {
            for (i <- 0 until numPlayers) {
                val request = game.inputManager.getInputRequest(i)
                if (request != null && request.answer.isEmpty) {
                    game.inputManager.answerInputRequest(i, players(i).makeDecision(request, game.getNormalizedGameState(i), game))
                }
            }
        }

        game.playerManager.players.foreach(p => players(p.playerId).endGame(p, game.playerManager.players))
    }

    def main(args: Array[String]) {
        if (args.length != 3) {

        }

        val rounds = 100//args(0).toInt
        val configuration = PlayerConfiguration.RandomTest//PlayerConfiguration.withName(args(1))
        val network_id = "blah"//args(2)

        //val joyOfWind = new BranchingNeuralNetworkBot("JoyOfWind")
        val crusadeOfDawn = new MonteCarloBot()

        val players = configuration match {
            case PlayerConfiguration.RandomTest => List(
                crusadeOfDawn,
                new RandomBot(),
                new RandomBot(),
                new RandomBot(),
                new RandomBot(),
                new RandomBot())
        }
        for (i <- 1 to rounds) {
            val startTime = System.currentTimeMillis()
            runGame(Random.shuffle(players))
            logger.info("Game " + i.toString + " time: " + (System.currentTimeMillis() - startTime))
        }

        for (player <- players) {
            player.endSession()
        }
        println("crusadeOfDawn:")
        crusadeOfDawn.endSession()
    }
}

object PlayerConfiguration extends Enumeration {
    type PlayerConfiguration = Value
    val RandomTraining, MutationTraining, RandomTest = Value
}
