import scala.collection.mutable.HashMap

object Runner {
  
  def runGame = {
    val numPlayers = 6
    val players = List(new AnnotatingRandomBot, new AnnotatingRandomBot, new AnnotatingRandomBot, new AnnotatingRandomBot, new AnnotatingRandomBot, new AnnotatingRandomBot)
    val game = new Game(numPlayers)

    while (game.makeProgress() != RetriableMethodResponse.Complete) {
      for (i <- 0 to numPlayers - 1) {
        val request = InputManager.getInputRequest(i)
        if (request != null && !request.answered) {
          InputManager.answerInputRequest(i, players(i).makeDecision(request, game.getGameState))
        }
      }
    }
    
    val winner = PlayerManager.players.maxBy( p => p.points )
    players(winner.playerId).uploadDecisions(Map[String, String]())
  }
  
  def main(args: Array[String]) {
    for (i <- 1 to 1) {
      runGame
    }
  }
}