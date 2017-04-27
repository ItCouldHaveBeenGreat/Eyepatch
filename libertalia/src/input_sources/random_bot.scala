import scala.util.Random

class RandomBot extends InputSource {
    def makeDecision(request: InputRequest, state: Seq[Int]) : String = {
        return request.validAnswers(Random.nextInt(request.validAnswers.size))
    }
}