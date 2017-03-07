import scala.math.Ordered.orderingToOrdered

abstract class Pirate(val player: Player) extends Ordered[Pirate] {
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
    def endOfVoyageAction = { }
    
    def claimBooty(round : Round) : RetriableMethodResponse.Value = {
        if (round.booty.size == 0) {
            println("Player " + player.playerId + " had no booty to claim")
            return RetriableMethodResponse.Complete
        }
        val validAnswers = round.booty.map(b => b.id.toString)
        val request = InputManager.postAndGetInputRequest(player.playerId, InputRequestType.SelectBooty, validAnswers)
        if (!request.answered) {
            return RetriableMethodResponse.PendingInput
        } else {
            val b = InputManager.getBootyFromInput(request)
            InputManager.removeInputRequest(request.playerId)
            
            // TODO: saber effect
            // TODO: spanish officer effect
            
            player.booty += b
            round.booty -= b
            println("Player " + player.playerId + " claims " + b)
            return RetriableMethodResponse.Complete
        }
    }
    
    def compare(that: Pirate) : Int = {
        if (rank == that.rank) {
            return subRank - this.subRank
        }
        return rank - that.rank
    }
    
    def tag : String = { return name + "(" + player.playerId + ")" }
    
    protected def getSubRank(player : Player) : Int
}