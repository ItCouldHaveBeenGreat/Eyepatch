import scala.collection.mutable.HashMap

object Runner {
  def main(args: Array[String]) {
    val numPlayers = 3

    val players = List(new RandomBot, new RandomBot, new QueryBot("https://[::1]:8080"))
    val game = new Game(numPlayers)

    var ticks = 0
    while (game.makeProgress() != RetriableMethodResponse.Complete) {
      for (i <- 0 to numPlayers - 1) {
        val request = InputManager.getInputRequest(i)
        // TODO: nulls are evil
        if (request != null && !request.answered) {
          InputManager.answerInputRequest(i, players(i).makeDecision(request, game.getGameState))
        }
      }
    }
    
  }
}