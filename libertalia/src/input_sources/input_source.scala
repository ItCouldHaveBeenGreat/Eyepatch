package input_sources

import libertalia.{InputRequest, Player}

trait InputSource {
    def makeDecision(request: InputRequest, state: Seq[Int]) : String
    def endGame(player: Player, players : Seq[Player])
    def endSession()
}