package main.java.piratebot.input_sources

import java.io.FileNotFoundException

import main.java.piratebot.InputRequestType.InputRequestType
import main.java.piratebot._
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.{NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.deeplearning4j.util.ModelSerializer
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.cpu.nativecpu.NDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.lossfunctions.LossFunctions

import scala.collection.mutable.ArrayBuffer

class AlphaBot(id: String, thinkingTimeInMilliseconds: Int = 5000) extends InputSource with Statistics {

    private val BOOTY_TYPE_OUTPUT_SIZE = Booty.maxId
    private val ANY_PIRATE_OUTPUT_SIZE = 30 * 6 + 1
    private val PLAYER_PIRATE_OUTPUT_SIZE = 30 + 1 // because I don't want to downshift all the pirate ranks
    private val BOOLEAN_OUTPUT_SIZE = 2

    private var gamesPlayed = 0

    private val TRAINING_BUFFER_THRESHOLD = 300

    val networks : Map[InputRequestType.Value, MultiLayerNetwork] = loadNetworks(id)
    val trainingDataBuffer : Map[InputRequestType.Value, ArrayBuffer[TrainingData]] = InputRequestType
        .values
        .toList
        .map(subType => subType -> new ArrayBuffer[TrainingData])
        .toMap

    override def makeDecision(request: InputRequest, state: Seq[Int], game: Game) : Int = {
        val searchTree = new MonteCarloSearchTree(game, request.playerId, request.choices.values.toList, request.inputType)//, neuralNetworkRollout)
        searchTree.playOut(request.playerId, thinkingTimeInMilliseconds)
        println(id + " did " + searchTree.rootNode.plays + " playouts")
        addTrainingData(
            new TrainingData(
                request.playerId,
                request.inputType,
                game.getNormalizedGameState(request.playerId),
                searchTree.getBestChoice,
                id)
        )
        searchTree.getBestChoice
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = {
        if (player.points == players.maxBy( p => p.points ).points) {
            addCounter("wins", 1)
        }
        addCounter("games", 1)
    }

    override def endSession(): Unit = {
        printCounters(id)
    }

    def addTrainingData(trainingDatum: TrainingData): Unit = {
        trainingDataBuffer(trainingDatum.decisionType) += trainingDatum
        if (trainingDataBuffer(trainingDatum.decisionType).size > TRAINING_BUFFER_THRESHOLD) {
            trainNetworkForSubType(trainingDatum.decisionType)
        }
    }

    def neuralNetworkRollout(inputType: InputRequestType, choices: List[Int], gameState: Seq[Int]): Int = {
        getBestValidChoice(gameState, inputType, choices)
    }

    def trainNetworkForSubType(subType : InputRequestType.Value): Unit = {
        val networkToTrain = networks(subType)
        val gameStates = Nd4j.create(trainingDataBuffer(subType).map(trainingDatum => trainingDatum.gameState.map(i => i.toFloat).toArray).toArray)
        val decisionsMade = Nd4j.create(trainingDataBuffer(subType).map(trainingDatum => expandExpectedOutput(trainingDatum.decision, getOutputSizeForSubType(subType))).toArray)
        trainingDataBuffer(subType).clear()
        try {
            networkToTrain.fit(gameStates, decisionsMade)
        } catch {
            case _: Exception =>
        }
        saveNetworks(id)
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
        val bestValidChoice = choiceWeights.indices.filter(index => choices.contains(index)).maxBy(choiceWeights)

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
            .optimizationAlgo(OptimizationAlgorithm.LBFGS)
            .weightInit(WeightInit.UNIFORM)
            .updater(Updater.NESTEROVS) //To configure: .updater(new Nesterovs(0.9))
            .list()
            .layer(0, new DenseLayer.Builder()
                .nIn(GameState.GAME_STATE_SIZE)
                .nOut(GameState.GAME_STATE_SIZE)
                .activation(Activation.RELU)
                .build())
            .layer(1, new DenseLayer.Builder()
                .nIn(GameState.GAME_STATE_SIZE)
                .nOut(GameState.GAME_STATE_SIZE)
                .activation(Activation.RELU)
                .build())
            .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .activation(Activation.SOFTMAX)
                .nIn(GameState.GAME_STATE_SIZE)
                .nOut(maximumOutput)
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

    def getNetworkId(key: String, subType: InputRequestType.Value): String = {
        key + "_" + subType
    }
}

