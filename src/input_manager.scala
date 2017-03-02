import scala.collection.mutable.HashMap
import scala.collection.mutable.Map

// TODO: Define class as the intersection of a player facing trait and a game facing trait
object InputManager {

    // TODO: Restructure this to be a set
    private val inputRequests : Map[Int, InputRequest] = HashMap[Int, InputRequest]()
    private var nextRequestId : Int = 0;


    def getInputRequest(playerId : Int) : InputRequest = {
        if (inputRequests.contains(playerId)) {
            return inputRequests(playerId)
        } else {
            return null
        }
    }
    
    def answerInputRequest(playerId: Int, inputResponse : String) = {
        val request : InputRequest = inputRequests(playerId)
        if (request.validAnswers.contains(inputResponse)) {
            request.answer = inputResponse
            println("Player " + playerId + " answered " + request.answer)
        } else {
            throw new Exception("Invalid answer")
        }
    }

    def postAndGetInputRequest(playerId: Int,
                               requestType: InputRequestType.Value,
                               validAnswers : Seq[String]) : InputRequest = {
        if (!inputRequests.contains(playerId)) {
            inputRequests += ((playerId, new InputRequest(playerId, requestType, validAnswers)))
        }
        return inputRequests(playerId)
    }
    
    def removeInputRequest(playerId : Int) = {
        inputRequests -= playerId
    }
}