package main.java.piratebot.input_sources
import main.java.piratebot.{InputRequest, Player}
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.conf.{NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.lossfunctions.LossFunctions

import scala.util.Random

class BranchingNeuralNetworkBot(key: String) extends InputSource with Statistics {
    

    override def makeDecision(request: InputRequest, state: Seq[Int]): String = {
        // currently a random bot
        val decision = request.validAnswers(Random.nextInt(request.validAnswers.size))
        return decision
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = {

    }

    override def endSession(): Unit = {

    }

    def buildNeuralNetwork(): MultiLayerNetwork = {
        val net = new MultiLayerNetwork(
            new NeuralNetConfiguration.Builder()
                .iterations(10)
                .optimizationAlgo(OptimizationAlgorithm.LINE_GRADIENT_DESCENT)
                .learningRate(0.01)
                .weightInit(WeightInit.UNIFORM)
                .updater(Updater.NESTEROVS)
                .list
                .layer(0, new DenseLayer.Builder()
                    .nIn(1)
                    .nOut(10)
                    .activation(Activation.TANH)
                    .build)
                .layer(1, new DenseLayer.Builder()
                    .nIn(10)
                    .nOut(10)
                    .activation(Activation.TANH)
                    .build)
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                    .activation(Activation.IDENTITY)
                    .nIn(10)
                    .nOut(1)
                    .build)
                .pretrain(false)
                .backprop(true)
                .build)
        net.init()

        net
    }
}
