abstract class Pirate(player: Player) {
    val rank : Int
    val subRank: Int = getSubRank(player)


    var state : PirateState.Value = PirateState.Deck
    
    
    // By default, a pirate only claims booty
    def dayAction(round : Round): RetriableMethodResponse.Value = {
        return RetriableMethodResponse.Complete
    }
    def duskAction(round : Round): RetriableMethodResponse.Value = {
        return RetriableMethodResponse.Complete
        // return claimBooty()
    }
    def nightAction(): RetriableMethodResponse.Value = {
        return RetriableMethodResponse.Complete
    }
    def endOfVoyageAction(): RetriableMethodResponse.Value = {
        return RetriableMethodResponse.Complete
    }
    
    protected def getSubRank(player : Player) : Int
}