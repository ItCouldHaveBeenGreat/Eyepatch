package main.java.piratebot.input_sources

import main.java.piratebot.InputRequestType

class TrainingData(val playerId : Int,
                   val decisionType : InputRequestType.Value,
                   val gameState : Seq[Int],
                   val decision : Int,
                   val agent : String) {

}

