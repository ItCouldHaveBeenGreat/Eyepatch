package main.java.piratebot

import input_sources.{RandomBot, _}

import scala.util.Random

object Runner {
    def runGame(players : List[InputSource]): Unit = {
        val numPlayers = players.size
        val game = new Game(numPlayers)

        while (game.makeProgress() != RetriableMethodResponse.Complete) {
            0.until(numPlayers)
                .filter(playerNum => !players(playerNum).getClass.isInstanceOf[StandardInputBot])
                .foreach { playerNum =>
                    val request = game.inputManager.getInputRequest(playerNum)
                    if (request != null && request.answer.isEmpty) {
                        game.inputManager.answerInputRequest(playerNum, players(playerNum).makeDecision(request, game.getNormalizedGameState(playerNum), game))
                    }
                }
            0.until(numPlayers)
                .filter(playerNum => players(playerNum).getClass.isInstanceOf[StandardInputBot])
                .foreach { playerNum =>
                    val request = game.inputManager.getInputRequest(playerNum)
                    if (request != null && request.answer.isEmpty) {
                        game.inputManager.answerInputRequest(playerNum, players(playerNum).makeDecision(request, game.getNormalizedGameState(playerNum), game))
                    }
                }
        }

        game.playerManager.players.foreach(p => players(p.playerId).endGame(p, game.playerManager.players))
    }

    def main(args: Array[String]) {
        if (args.length != 3) {

        }

        val rounds = 1//args(0).toInt
        val configuration = PlayerConfiguration.RandomTest//PlayerConfiguration.withName(args(1))

        //val joyOfWind = new BranchingNeuralNetworkBot("JoyOfWind")
        val crusadeOfDawn = new RecordingMonteCarloBot("Monte Carlo Bot", 1000)
        val joyOfWind = new AlphaBot("JoyOfWind", 500)
        val marchOfTime = new AlphaBot("MarchOfTime", 5000)
        val vanillaMarchOfTime = new BranchingNeuralNetworkBot("MarchOfTime")

        val players = configuration match {
            case PlayerConfiguration.RandomTest => List(
                crusadeOfDawn,
                //new StandardInputBot("Thomas"),
                //new BranchingNeuralNetworkBot("MarchOfTime"),
                //new RandomBot(),
                //new BranchingNeuralNetworkBot("JoyOfWind"),
                new RandomBot(),
                new RandomBot(),
                new RandomBot(),
                new RandomBot())
        }
        for (i <- 1 to rounds) {
            val startTime = System.currentTimeMillis()
            runGame(Random.shuffle(players))
            println("Game " + i.toString + " time: " + (System.currentTimeMillis() - startTime))
        }

        for (player <- players) {
            player.endSession()
        }
    }
}

object PlayerConfiguration extends Enumeration {
    type PlayerConfiguration = Value
    val RandomTraining, MutationTraining, RandomTest = Value
}
