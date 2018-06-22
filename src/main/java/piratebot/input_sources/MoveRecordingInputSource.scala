package main.java.piratebot.input_sources

import java.io.File

import main.java.piratebot._
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.factory.Nd4j

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

abstract class MoveRecordingInputSource(filename: String) extends InputSource {
    val trainingData: Map[InputRequestType.Value, ArrayBuffer[TrainingData]] = InputRequestType
      .values
      .toList
      .map(subType => subType -> new ArrayBuffer[TrainingData])
      .toMap
    var decisionsMade = 0
    val SAVE_THRESHOLD = 200

    def makeDecision(request: InputRequest, state: Seq[Int], game: Game): Int = {
        val decision = makeRecordedDecision(request, state, game)
        trainingData(request.inputType) += new TrainingData(request.playerId, request.inputType, state, decision, "")

        if (trainingData(request.inputType).size > SAVE_THRESHOLD) {
            saveData(request.inputType)
        }

        decision
    }

    def endSession(): Unit = {
        println("Writing data for " + filename)
    }

    def makeRecordedDecision(request: InputRequest, state: Seq[Int], game: Game): Int

    private def saveData(decisionType: InputRequestType.Value): Unit = {
        println("Writing data for " + filename)
        val gameStates = Nd4j.create(trainingData(decisionType).map(trainingDatum => trainingDatum.gameState.map(i => i.toFloat).toArray).toArray)
        val decisionsMade = Nd4j.create(trainingData(decisionType).map(trainingDatum => expandExpectedOutput(trainingDatum.decision, getOutputSizeForSubType(trainingDatum.decisionType))).toArray)
        trainingData(decisionType).clear()

        val incrementalDataSet = new DataSet(gameStates, decisionsMade)

        val dataSetFile = new File(filename + "_" + decisionType.toString + ".data")

        if (dataSetFile.exists()) {
            println("Loaded from: " + dataSetFile.getAbsolutePath)
            val currentDataSet = new DataSet()
            currentDataSet.load(dataSetFile)
            val combinedDataSet = DataSet.merge(List(currentDataSet, incrementalDataSet).asJava)
            combinedDataSet.save(dataSetFile)
        } else {
            incrementalDataSet.save(dataSetFile)
        }
        println("Saved to: " + dataSetFile.getAbsolutePath)
    }

    private def expandExpectedOutput(toConvert: Int, maximumValue: Int) : Array[Float] = {
        val expandedForm = Array.fill(maximumValue)(0.0f)
        expandedForm(toConvert) = 1.0f
        expandedForm
    }

    private val BOOTY_TYPE_OUTPUT_SIZE = Booty.maxId
    private val ANY_PIRATE_OUTPUT_SIZE = 30 * 6 + 1
    private val PLAYER_PIRATE_OUTPUT_SIZE = 30 + 1 // because I don't want to downshift all the pirate ranks
    private val BOOLEAN_OUTPUT_SIZE = 2
    private def getOutputSizeForSubType(subType: InputRequestType.Value) : Int = {
        subType match {
            case InputRequestType.DiscardAllButOneBooty | InputRequestType.SelectBooty | InputRequestType.SellBooty =>
                BOOTY_TYPE_OUTPUT_SIZE
            case InputRequestType.KillPirateInAdjacentDen | InputRequestType.KillPirateInAnyDen =>
                ANY_PIRATE_OUTPUT_SIZE
            case InputRequestType.PlayPirateFromHand | InputRequestType.RecruitPirateFromDen | InputRequestType.RevivePirateFromDiscard =>
                PLAYER_PIRATE_OUTPUT_SIZE
            case InputRequestType.SellMap | InputRequestType.SellThreeBooty =>
                BOOLEAN_OUTPUT_SIZE
            case _ => throw new RuntimeException("Unknown InputRequestType: " + subType.toString)
        }
    }
}