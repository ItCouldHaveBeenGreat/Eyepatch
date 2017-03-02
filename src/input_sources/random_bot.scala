import scala.util.Random

class RandomBot extends InputSource {
    def makeDecision(request: InputRequest, state: GameState) : String = {
        return request.validAnswers(Random.nextInt(request.validAnswers.size))
    }
}