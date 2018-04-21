package main.java.piratebot.input_sources

import main.java.piratebot.InputRequestType.InputRequestType

import scala.collection.mutable.ArrayBuffer

trait Annotating {
    private val trainingData = ArrayBuffer[TrainingData]()

    protected def record(playerId : Int, agent : String, inputType : InputRequestType, decision : Int, state : Seq[Int]) = {
        trainingData += new TrainingData(playerId, inputType, state, decision.toInt, agent)
    }

    protected def getData : ArrayBuffer[TrainingData] = {
        trainingData
    }

    def clearData = {
        trainingData.clear()
    }
}
