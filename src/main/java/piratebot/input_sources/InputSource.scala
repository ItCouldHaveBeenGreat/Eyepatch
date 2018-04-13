package main.java.piratebot.input_sources

import main.java.piratebot.{InputRequest, Player}

trait InputSource {
    def makeDecision(request: InputRequest, state: Seq[Int]) : String
    def endGame(player: Player, players : Seq[Player])
    def endSession()
}