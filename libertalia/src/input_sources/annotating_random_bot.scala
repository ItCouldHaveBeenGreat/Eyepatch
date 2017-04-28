import scala.collection.mutable.ArrayBuffer
import scala.util.Random

import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient

class AnnotatingRandomBot() extends InputSource {
    val OPENSHIFT = "http://rutsubo-thewindwhispers.rhcloud.com"
    val UPLOAD_TRAINING_DATA = "load_training_data"
    val CREATE_NETWORK = "create_network"
    val agent = "randombot"
    
    val trainingData = ArrayBuffer[TrainingData]()
    
    def makeDecision(request: InputRequest, state: Seq[Int]) : String = {
        val decision = request.validAnswers(Random.nextInt(request.validAnswers.size))
        trainingData += new TrainingData(request.playerId, request.inputType.id, state, decision.toInt, agent)
        return decision
    }
    
    def uploadDecisions(annotations : Map[String, String]) = {
        trainingData.foreach ( td => td.setAnnotation(annotations) )
        
        val post = new HttpPost(OPENSHIFT + "/" + UPLOAD_TRAINING_DATA)
        post.setHeader("Content-type", "application/json")
        post.setEntity(new StringEntity(buildJson))
        val response = (new DefaultHttpClient).execute(post)
        
        println("AnnotatingRandomBot: " + response)
    }
    
    def buildJson : String = {
        return "[" + trainingData.map( td => td.buildJson ).mkString(",") + "]"
    }
}