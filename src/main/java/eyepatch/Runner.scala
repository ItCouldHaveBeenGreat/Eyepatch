package main.java.eyepatch

import input_sources._

import scala.util.Random

object Runner {



  def runGame(players : List[InputSource]) = {
    val numPlayers = players.size

    val game = new Game(numPlayers)

    while (game.makeProgress() != RetriableMethodResponse.Complete) {
      for (i <- 0 to numPlayers - 1) {
        val request = InputManager.getInputRequest(i)
        if (request != null && !request.answered) {
          InputManager.answerInputRequest(i, players(i).makeDecision(request, game.getNormalizedGameState(i)))
        }
      }
    }

    PlayerManager.players.foreach(p => players(p.playerId).endGame(p, PlayerManager.players))
  }

  def main(args: Array[String]) {
    OutputManager.enableChannel(Channel.Bot)
    //OutputManager.enableChannel(Channel.Debug)
    //OutputManager.enableChannel(Channel.Game)
    //OutputManager.enableChannel(Channel.Pirate)
    OutputManager.enableChannel(Channel.Runner)

    if (args.length != 3) {

    }


    val rounds = args(0).toInt
    val configuration = PlayerConfiguration.withName(args(1))
    val network_id = args(2)

    val players = configuration match {
      case PlayerConfiguration.RandomTraining => List(
        new RemoteNeuralNetworkBot(network_id),
        new AnnotatingRandomBot(network_id),
        new AnnotatingRandomBot(network_id),
        new AnnotatingRandomBot(network_id),
        new AnnotatingRandomBot(network_id),
        new AnnotatingRandomBot(network_id))
      case PlayerConfiguration.MutationTraining => List(
        new RemoteNeuralNetworkBot(network_id),
        new RemoteNeuralNetworkBot(network_id),
        new AnnotatingMutatingBot(network_id, 0.01),
        new AnnotatingMutatingBot(network_id, 0.05),
        new RandomBot(),
        new RandomBot())
      case PlayerConfiguration.RandomTest => List(
        new RemoteNeuralNetworkBot(network_id),
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
