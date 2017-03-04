import scala.util.Random

class FANNBot extends InputSource {
    def makeDecision(request: InputRequest, state: GameState) : String = {
        return request.validAnswers(Random.nextInt(request.validAnswers.size))
    }
}