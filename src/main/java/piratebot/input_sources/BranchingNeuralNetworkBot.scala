package main.java.piratebot.input_sources

import java.io.FileNotFoundException

import main.java.piratebot._
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.conf.{NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.cpu.nativecpu.NDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.lossfunctions.LossFunctions

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class BranchingNeuralNetworkBot(val network_id : String, val shouldTrain: Boolean = false) extends InputSource with Statistics {

    private val BOOTY_TYPE_OUTPUT_SIZE = Booty.maxId
    private val ANY_PIRATE_OUTPUT_SIZE = 30 * 6 + 1
    private val PLAYER_PIRATE_OUTPUT_SIZE = 30 + 1 // because I don't want to downshift all the pirate ranks
    private val BOOLEAN_OUTPUT_SIZE = 2

    private var gamesPlayed = 0

    private val TRAINING_BUFFER_THRESHOLD = 200

    val networks : Map[InputRequestType.Value, MultiLayerNetwork] = loadNetworks(network_id)
    val trainingDataBuffer : Map[InputRequestType.Value, ArrayBuffer[TrainingData]] = InputRequestType
        .values
        .toList
        .map(subType => subType -> new ArrayBuffer[TrainingData])
        .toMap

    override def makeDecision(request: InputRequest, state: Seq[Int], game: Game) : Int = {
        getBestValidChoice(state, request.inputType, request.choices.values.toList)
        //return request.choices.values.toList(Random.nextInt(request.choices.values.size))
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = {
        gamesPlayed += 1
        if (player.points == players.maxBy( p => p.points ).points) {
            addCounter("wins", 1)
        }
        addCounter("games", 1)

        if (gamesPlayed % 50 == 0) {
            saveNetworks(network_id)
        }
    }

    override def endSession(): Unit = {
        printCounters(network_id)
    }

    def consume(trainingData: Seq[TrainingData]): Unit = {
        trainingData.foreach(trainingDatum => trainingDataBuffer(trainingDatum.decisionType) += trainingDatum)
        trainingDataBuffer.foreach { case(subType, trainingBuffer) =>
            if (trainingBuffer.size > TRAINING_BUFFER_THRESHOLD) {
                //trainNetworkForSubType(subType)
            }
        }
    }

    def trainNetworkForSubType(subType : InputRequestType.Value): Unit = {
        if (!shouldTrain) {
            return
        }

        val networkToTrain = networks(subType)
        val gameStates = Nd4j.create(trainingDataBuffer(subType).map(trainingDatum => trainingDatum.gameState.map(i => i.toFloat).toArray).toArray)
        val decisionsMade = Nd4j.create(trainingDataBuffer(subType).map(trainingDatum => expandExpectedOutput(trainingDatum.decision, getOutputSizeForSubType(subType))).toArray)
        trainingDataBuffer(subType).clear()
        try {
            networkToTrain.fit(gameStates, decisionsMade)
        } catch {
            case _: Exception =>
        }
    }

    def expandExpectedOutput(toConvert: Int, maximumValue: Int) : Array[Float] = {
        val expandedForm = Array.fill(maximumValue)(0.0f)
        expandedForm(toConvert) = 1.0f
        return expandedForm
    }

    def getBestValidChoice(gameState: Seq[Int], inputType: InputRequestType.Value, choices: List[Int]) : Int = {
        if (choices.size == 1) {
            return choices.head
        }

        val floatState = gameState.map(i => i.toFloat).toArray
        val choiceWeights = networks(inputType).output(new NDArray(floatState)).data().asFloat()
        println("choiceWeights: " + choiceWeights.mkString(", "))
        val bestValidChoice = choiceWeights.indices.filter(index => choices.contains(index)).maxBy(choiceWeights)
        println("choices: " + choices)
        println("best: " + bestValidChoice)

        bestValidChoice
    }

    def buildNeuralNetworkForSubType(subType: InputRequestType.Value) : MultiLayerNetwork = {
        buildNeuralNetwork(getOutputSizeForSubType(subType))
    }

    def getOutputSizeForSubType(subType: InputRequestType.Value) : Int = {
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

    def buildNeuralNetwork(maximumOutput: Int) : MultiLayerNetwork = {
        val network = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
            .iterations(10)
            .optimizationAlgo(OptimizationAlgorithm.LBFGS)
            .learningRate(0.10)
            .weightInit(WeightInit.UNIFORM)
            .updater(Updater.NESTEROVS) //To configure: .updater(new Nesterovs(0.9))
            .list()
            .layer(0, new DenseLayer.Builder()
                .nIn(GameState.GAME_STATE_SIZE)
                .nOut(GameState.GAME_STATE_SIZE)
                .activation(Activation.RELU)
                    .l2(0.5)
                .build())
            .layer(1, new DenseLayer.Builder()
                .nIn(GameState.GAME_STATE_SIZE)
                .nOut(GameState.GAME_STATE_SIZE)
                .activation(Activation.RELU)
                .l2(0.5)
                .build())
            .layer(2, new DenseLayer.Builder()
                .nIn(GameState.GAME_STATE_SIZE)
                .nOut(GameState.GAME_STATE_SIZE)
                .activation(Activation.RELU)
                .l2(0.5)
                .build())
            .layer(3, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .activation(Activation.SOFTMAX)
                .nIn(GameState.GAME_STATE_SIZE)
                .nOut(maximumOutput)
                .l2(0.5)
                .build())
            .pretrain(false)
            .backprop(true)
            .build()
        )
        network.init()
        network.setListeners(new ScoreIterationListener(10))
        network
    }

    def loadNetworks(key: String) : Map[InputRequestType.Value, MultiLayerNetwork] = {
        InputRequestType.values.toStream
            .map{ subType =>
                try {
                    subType -> ModelSerializer.restoreMultiLayerNetwork(getNetworkId(key, subType))
                } catch {
                    case _: FileNotFoundException => subType -> buildNeuralNetworkForSubType(subType)
                }
            }.toMap
    }

    def saveNetworks(key: String) : Unit = {
        networks.foreach{ case(subType, network) =>
            ModelSerializer.writeModel(network, getNetworkId(key, subType), true)
        }
    }

    def getNetworkId(key: String, subType: InputRequestType.Value) = {
        key + "_" + subType
    }
}
