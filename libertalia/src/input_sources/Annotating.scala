package input_sources

import libertalia.InputRequestType.InputRequestType

import scala.collection.mutable.ArrayBuffer

trait Annotating {
  private val trainingData = ArrayBuffer[TrainingData]()

  protected def record(playerId : Int, agent : String, inputType : InputRequestType, decision : Int, state : Seq[Int]) = {
    trainingData += new TrainingData(playerId, inputType.id, state, decision.toInt, agent)
  }

  def annotate(annotations : Map[String, String]) = {
    trainingData.foreach(td => td.setAnnotation(annotations))
  }

  protected def getAnnotatedData : String = {
    return "[" + trainingData.map( td => td.buildJson ).mkString(",") + "]"
  }

  def clearData() = {
    trainingData.clear()
  }
}
