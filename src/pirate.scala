abstract class Pirate(player: Player) {
    val rank : Int
    val name : String
    val visible : Boolean = true
    val subRank: Int = getSubRank(player)


    var state : PirateState.Value = PirateState.Deck
    
    
    // By default, a pirate only claims booty
    def dayAction(round : Round): RetriableMethodResponse.Value = {
        return RetriableMethodResponse.Complete
    }
    def duskAction(round : Round): RetriableMethodResponse.Value = {
        return claimBooty(round)
    }
    def nightAction: RetriableMethodResponse.Value = {
        return RetriableMethodResponse.Complete
    }
    def endOfVoyageAction: RetriableMethodResponse.Value = {
        return RetriableMethodResponse.Complete
    }
    
    def claimBooty(round : Round) : RetriableMethodResponse.Value = {
        val validAnswers = round.booty.map(b => b.id.toString)
        val request = InputManager.postAndGetInputRequest(player.playerId, InputRequestType.SelectBooty, validAnswers)
        if (request.answered) {
            return RetriableMethodResponse.PendingInput
        } else {
            val b = InputManager.getBootyFromInput(request)
            player.booty += b
            println("Player " + player.playerId + " claims " + b)
            return RetriableMethodResponse.Complete
        }
    }
    
    def tag : String = { return name + "(" + player.playerId + ")" }
    
    protected def getSubRank(player : Player) : Int
}