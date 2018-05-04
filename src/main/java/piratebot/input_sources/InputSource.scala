package main.java.piratebot.input_sources

import main.java.piratebot.{Game, InputRequest, Player}

trait InputSource {
    def makeDecision(request: InputRequest, state: Seq[Int], game: Game) : Int
    def endGame(player: Player, players : Seq[Player])
    def endSession()
}