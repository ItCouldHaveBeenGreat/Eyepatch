package main.java.piratebot.input_sources

import main.java.piratebot.{Game, InputRequest, Player}

trait InputSource {
    def makeDecision(request: InputRequest, state: Seq[Int], game: Game) : Int
    def endGame(player: Player, players : Seq[Player])
    def endSession()

    def getPlayerColor(playerNum: Int): String = {
        playerNum match {
            case 0 => "Red"
            case 1 => "Yellow"
            case 2 => "Green"
            case 3 => "Blue"
            case 4 => "Black"
            case 5 => "White"
            case _ => throw new RuntimeException("Unknown color for player number " + playerNum)
        }
    }
}