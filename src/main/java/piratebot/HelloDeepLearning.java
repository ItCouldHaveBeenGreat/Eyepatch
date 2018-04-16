package main.java.piratebot;

import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RBM;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HelloDeepLearning {

    private static final Logger log = LoggerFactory.getLogger(HelloDeepLearning.class);

    private static DataSetIterator getTrainingData(final INDArray x, final int batchSize, final Random rng) {
        final INDArray y = x.mul(2);
        final DataSet allData = new DataSet(x,y);

        final List<DataSet> list = allData.asList();
        Collections.shuffle(list,rng);
        return new ListDataSetIterator(list,batchSize);
    }

    public static void main(String[] args) throws Exception {
        int nEpochs = 50;
        int iterations = 1;

        //Initialize the user interface backend
        UIServer uiServer = UIServer.getInstance();

        //Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
        StatsStorage statsStorage = new InMemoryStatsStorage();         //Alternative: new FileStatsStorage(File), for saving and loading later

        //Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
        uiServer.attach(statsStorage);




        log.info("Network configuration and training...");
        MultiLayerNetwork net = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .iterations(iterations)
                .optimizationAlgo(OptimizationAlgorithm.LINE_GRADIENT_DESCENT)
                .learningRate(0.01)
                .weightInit(WeightInit.UNIFORM)
                .updater(Updater.NESTEROVS)     //To configure: .updater(new Nesterovs(0.9))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(1).nOut(10)
                        .activation(Activation.TANH)
                        .build())
                .layer(1, new DenseLayer.Builder().nIn(10).nOut(10)
                        .activation(Activation.TANH)
                        .build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(10).nOut(1).build())
                .pretrain(false)
                .backprop(true)
                .build()
        );
        net.init();
        net.setListeners(new ScoreIterationListener(10));
        log.debug("Total num of params: {}", net.numParams());

        float[][] rawInput = new float[5000][1];
        for (int i = 0; i < rawInput.length; i++) {
            rawInput[i] = new float[]{(float) (i / 500)};
        }
        INDArray input = new NDArray(rawInput);
        DataSetIterator trainingSet = getTrainingData(input, 100, new Random(1));

        //Then add the StatsListener to collect this information from the network, as it trains
        net.setListeners(new StatsListener(statsStorage));


        // evaluation while training (the score should go down)
        for (int i = 0; i < nEpochs; i++) {
            net.fit(trainingSet);
            log.info("Completed epoch {}", i);
            trainingSet.reset();
            //Evaluation eval = net.evaluate(trainingSet);
            //log.info(eval.stats());
            //trainingSet.reset();
        }
        trainingSet.reset();
        while (trainingSet.hasNext()) {
            DataSet ds = trainingSet.next();
            System.out.println(ds.toString());
        }

        final INDArray test = Nd4j.create(new double[] {0.5});
        INDArray out = net.output(test, false);
        System.out.println((out.toString()));

        System.out.println(net.output(new NDArray(new float[]{5.0f})));
        System.out.println(net.output(new NDArray(new float[]{4.0f})));
        System.out.println(net.output(new NDArray(new float[]{3.0f})));
        System.out.println(net.output(new NDArray(new float[]{2.0f})));
        System.out.println(net.predict(new NDArray(new float[]{10.0f}))[0]);
        uiServer.stop();
    }
}