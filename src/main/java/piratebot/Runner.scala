package main.java.piratebot

import input_sources._

import scala.util.Random

object Runner {

  def runGame(players : List[InputSource]) = {
    val numPlayers = players.size

    val game = new Game(numPlayers)

    while (game.makeProgress() != RetriableMethodResponse.Complete) {
      for (i <- 0 until numPlayers) {
        val request = InputManager.getInputRequest(i)
        if (request != null && request.answer.isEmpty) {
          InputManager.answerInputRequest(i, players(i).makeDecision(request, game.getNormalizedGameState(i)))
        }
      }
    }

    PlayerManager.players.foreach(p => players(p.playerId).endGame(p, PlayerManager.players))
  }

  def main(args: Array[String]) {
    OutputManager.enableChannel(Channel.Bot)
    //OutputManager.enableChannel(Channel.Debug)
    OutputManager.enableChannel(Channel.Game)
    OutputManager.enableChannel(Channel.Pirate)
    OutputManager.enableChannel(Channel.Runner)

    if (args.length != 3) {

    }


    val rounds = 1//args(0).toInt
    val configuration = PlayerConfiguration.RandomTest//PlayerConfiguration.withName(args(1))
    val network_id = "blah"//args(2)

    val players = configuration match {
      case PlayerConfiguration.RandomTest => List(
        new BranchingNeuralNetworkBot("JoyOfWind"),
        new RandomBot(),
        new RandomBot(),
        new RandomBot(),
        new RandomBot(),
        new RandomBot())
    }
    for (i <- 1 to rounds) {
      val startTime = System.currentTimeMillis()
      runGame(Random.shuffle(players))
      OutputManager.print(Channel.Runner, "Game " + i.toString() + " time: " + (System.currentTimeMillis() - startTime))
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
