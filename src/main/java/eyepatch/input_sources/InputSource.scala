package main.java.eyepatch.input_sources

import main.java.eyepatch.{InputRequest, Player}

trait InputSource {
    def makeDecision(request: InputRequest, state: Seq[Int]) : String
    def endGame(player: Player, players : Seq[Player])
    def endSession()
}