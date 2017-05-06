import input_sources.{AnnotatingRandomBot, FirstBot}
import libertalia._

object Runner {
  
  def runGame = {
    val numPlayers = 6
    val players = List(new AnnotatingRandomBot, new FirstBot, new AnnotatingRandomBot, new AnnotatingRandomBot, new AnnotatingRandomBot, new AnnotatingRandomBot)
    val game = new Game(numPlayers)

    while (game.makeProgress() != RetriableMethodResponse.Complete) {
      for (i <- 0 to numPlayers - 1) {
        val request = InputManager.getInputRequest(i)
        if (request != null && !request.answered) {
          InputManager.answerInputRequest(i, players(i).makeDecision(request, game.getGameState))
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

    for (i <- 1 to 1) {
      runGame
      OutputManager.print(Channel.Runner, i.toString)
    }
  }
}