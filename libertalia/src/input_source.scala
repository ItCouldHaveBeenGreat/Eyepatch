trait InputSource {
    def makeDecision(request: InputRequest, state: GameState) : String
}