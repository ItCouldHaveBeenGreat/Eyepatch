import input_sources.{AnnotatingRandomBot, FirstBot, InputSource}
import libertalia._

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

    val players = List(
      new FirstBot,
      new AnnotatingRandomBot,
      new AnnotatingRandomBot,
      new AnnotatingRandomBot,
      new AnnotatingRandomBot,
      new AnnotatingRandomBot)
    
    for (i <- 1 to 1) {
      runGame(Random.shuffle(players))
      OutputManager.print(Channel.Runner, i.toString)
    }

    for (player <- players) {
      player.endSession()
    }
  }
}