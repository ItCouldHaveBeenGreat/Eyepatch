package main.java.piratebot.input_sources

trait TrainingDataConsumer {
    def consume(trainingData: Seq[TrainingData]) : Unit
}
