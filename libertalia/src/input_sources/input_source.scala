package input_sources

trait InputSource {
    def makeDecision(request: InputRequest, state: Seq[Int]) : String
}