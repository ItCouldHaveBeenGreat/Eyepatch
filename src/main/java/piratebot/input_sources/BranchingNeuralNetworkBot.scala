package main.java.piratebot.input_sources

import main.java.piratebot._
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.conf.{NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.cpu.nativecpu.NDArray
import org.nd4j.linalg.lossfunctions.LossFunctions

import scala.util.Random

class BranchingNeuralNetworkBot(val network_id : String) extends InputSource with Annotating with Statistics {

    private val BOOTY_TYPE_OUTPUT_SIZE = Booty.maxId - 1
    private val ANY_PIRATE_OUTPUT_SIZE = 30 * 6 - 1
    private val PLAYER_PIRATE_OUTPUT_SIZE = 30 - 1
    private val BOOLEAN_OUTPUT_SIZE = 2

    val networks : Map[InputRequestType.Value, MultiLayerNetwork] = Map(
        InputRequestType.DiscardAllButOneBooty -> buildNeuralNetwork(BOOTY_TYPE_OUTPUT_SIZE),
        InputRequestType.KillPirateInAdjacentDen -> buildNeuralNetwork(ANY_PIRATE_OUTPUT_SIZE),
        InputRequestType.KillPirateInAnyDen -> buildNeuralNetwork(ANY_PIRATE_OUTPUT_SIZE),
        InputRequestType.PlayPirateFromHand -> buildNeuralNetwork(PLAYER_PIRATE_OUTPUT_SIZE),
        InputRequestType.RecruitPirateFromDen -> buildNeuralNetwork(PLAYER_PIRATE_OUTPUT_SIZE),
        InputRequestType.SelectBooty -> buildNeuralNetwork(BOOTY_TYPE_OUTPUT_SIZE),
        InputRequestType.RevivePirateFromDiscard -> buildNeuralNetwork(PLAYER_PIRATE_OUTPUT_SIZE),
        InputRequestType.SellBooty -> buildNeuralNetwork(BOOTY_TYPE_OUTPUT_SIZE),
        InputRequestType.SellMap -> buildNeuralNetwork(BOOLEAN_OUTPUT_SIZE),
        InputRequestType.SellThreeBooty -> buildNeuralNetwork(BOOLEAN_OUTPUT_SIZE))

    override def makeDecision(request: InputRequest, state: Seq[Int]) : Int = {
        val floatState = state.map(i => i.toFloat).toArray

        println("RESPONSE: " + networks(request.inputType).output(new NDArray(floatState)))
        println("RESPONSE: " + networks(request.inputType).predict(new NDArray(floatState)).mkString(", "))
        return request.choices.values.toList(Random.nextInt(request.choices.values.size))
    }

    override def endGame(player: Player, players: Seq[Player]) = {

    }

    override def endSession() = {

    }

    def getBestValidChoice(gameState: Seq[Int], inputType: InputRequestType.Value, choices: List[Int]) : Int = {
        val floatState = gameState.map(i => i.toFloat).toArray
        val choiceWeights = networks(inputType).output(new NDArray(floatState))
        1
    }

    def buildNeuralNetwork(maximumOutput: Int) : MultiLayerNetwork = {
        val network = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
            .iterations(10)
            .optimizationAlgo(OptimizationAlgorithm.LBFGS)
            .learningRate(0.01)
            .weightInit(WeightInit.DISTRIBUTION)
            .updater(Updater.NESTEROVS) //To configure: .updater(new Nesterovs(0.9))
            .list()
            .layer(0, new DenseLayer.Builder()
                .nIn(GameState.GAME_STATE_SIZE)
                .nOut(GameState.GAME_STATE_SIZE)
                .activation(Activation.TANH)
                .build())
            .layer(1, new DenseLayer.Builder()
                .nIn(GameState.GAME_STATE_SIZE)
                .nOut(GameState.GAME_STATE_SIZE)
                .activation(Activation.TANH)
                .build())
            .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .activation(Activation.IDENTITY)
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
}
