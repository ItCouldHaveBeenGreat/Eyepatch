package main.java.piratebot.input_sources

import java.io.File

import com.google.common.collect.ImmutableList
import main.java.piratebot.{Game, InputRequest, Player}
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.factory.Nd4j

import scala.collection.mutable.ArrayBuffer

abstract class MoveRecordingInputSource(filename: String) extends InputSource {
    val trainingData: ArrayBuffer[TrainingData] = ArrayBuffer.empty

    def makeDecision(request: InputRequest, state: Seq[Int], game: Game): Int = {
        val decision = makeRecordedDecision(request, state, game)
        trainingData += new TrainingData(request.playerId, request.inputType, state, decision, "")

        decision
    }

    def endGame(player: Player, players : Seq[Player])

    def endSession(): Unit = {
        saveData()
    }

    def makeRecordedDecision(request: InputRequest, state: Seq[Int], game: Game): Int

    private def saveData(): Unit = {
        val gameStates = Nd4j.create(trainingData.map(trainingDatum => trainingDatum.gameState.map(i => i.toFloat).toArray).toArray)
        val decisionsMade = Nd4j.create(trainingData.map(trainingDatum => expandExpectedOutput(trainingDatum.decision, getOutputSizeForSubType(subType))).toArray)
        trainingData.clear()

        val incrementalDataSet = new DataSet(gameStates, decisionsMade)

        val dataSetFile = new File(filename)
        val currentDataSet = new DataSet()
        currentDataSet.load(dataSetFile)
        val combinedDataSet = DataSet.merge(ImmutableList.of(currentDataSet, incrementalDataSet))
        combinedDataSet.save(dataSetFile)
    }

    private def expandExpectedOutput(toConvert: Int, maximumValue: Int) : Array[Float] = {
        val expandedForm = Array.fill(maximumValue)(0.0f)
        expandedForm(toConvert) = 1.0f
        expandedForm
    }
}