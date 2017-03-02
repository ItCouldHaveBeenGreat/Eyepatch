object Runner {
  def main(args: Array[String]) {
    val numPlayers = 3

    val game = new Game(numPlayers)

    val players = List(new RandomBot, new RandomBot, new RandomBot)
    
    val maxTicks = 100
    var ticks = 0
    while (game.makeProgress() != RetriableMethodResponse.Complete) {
      ticks += 1
      if (ticks > maxTicks) {
        return;
      }
      for (i <- 0 to numPlayers - 1) {
        val request = InputManager.getInputRequest(i)
        // TODO: nulls are evil
        if (request != null) {
          InputManager.answerInputRequest(i, players(i).makeDecision(request, game.getGameState))
        }
      }
    }
    
  }
}