import java.util

import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity

import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicNameValuePair

class AnnotatingRandomBot() extends InputSource {
    val OPENSHIFT = "http://rutsubo-thewindwhispers.rhcloud.com"
    val UPLOAD_TRAINING_DATA = "train_network"
    val CREATE_NETWORK = "create_network"
    val network_id = "first"
    val agent = "randombot"

    
    val trainingData = ArrayBuffer[TrainingData]()
    
    def makeDecision(request: InputRequest, state: Seq[Int]) : String = {
        val decision = request.validAnswers(Random.nextInt(request.validAnswers.size))
        trainingData += new TrainingData(request.playerId, request.inputType.id, state, decision.toInt, agent)
        return decision
    }

    def uploadDecisions(annotations : Map[String, String]) = {
        try {
            trainingData.foreach(td => td.setAnnotation(annotations))

            val create_network = new HttpPost(OPENSHIFT + "/" + CREATE_NETWORK)
            create_network.setHeader("Content-type", "application/x-www-form-urlencoded")
            create_network.setEntity(new StringEntity("network_id=" + network_id))
            val create_network_response = (new DefaultHttpClient).execute(create_network)
            OutputManager.print(Channel.Debug, "AnnotatingRandomBot: " + create_network_response)

            val train_network = new HttpPost(OPENSHIFT + "/" + UPLOAD_TRAINING_DATA)
            train_network.setHeader("Content-type", "application/x-www-form-urlencoded")
            val nameValuePairs = new util.ArrayList[NameValuePair](1)
            nameValuePairs.add(new BasicNameValuePair("network_id", network_id));
            nameValuePairs.add(new BasicNameValuePair("data", buildJson));
            train_network.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            val train_network_response = (new DefaultHttpClient).execute(train_network)

            OutputManager.print(Channel.Debug, "AnnotatingRandomBot: " + train_network_response)
        } catch {
            case e: Exception => println(e)
        }
    }
    
    def buildJson : String = {
        return "[" + trainingData.map( td => td.buildJson ).mkString(",") + "]"
    }
}