package libertalia

class Surgeon(player: Player) extends Pirate(player) {
    val rank = 22
    val name = "Surgeon"

    override def dayAction(round : Round): RetriableMethodResponse.Value = {
        if (InputManager.getPlayerDiscardFromPlayer(player).size == 0) {
            OutputManager.print(Channel.Pirate, tag + ": No one to revive")
            return RetriableMethodResponse.Complete
        }
        
        val request = InputManager.postAndGetInputRequest(
                player.playerId,
                InputRequestType.RevivePirateFromDiscard,
                InputManager.getPlayerDiscardFromPlayer(player))
        if (!request.answered) {
            return RetriableMethodResponse.PendingInput
        }
        val pirateRank = InputManager.getPirateIdFromInput(request)
        InputManager.removeInputRequest(request.playerId)
        
        val pirateToRevive = PlayerManager.players(request.playerId).getPirate(pirateRank)
        pirateToRevive.state = PirateState.Hand
        
        if (player.pirates.count( p => p.state == PirateState.Discard ) > 0) {
            // if there is a pirate remaining in the graveyard,
            // the entire graveyard and the revived pirate become unknown
            pirateToRevive.known = false
            for (p <- player.pirates) {
                if (p.state == PirateState.Discard) {
                    p.known = false
                }
            }
        } else {
            // if the graveyard is emptied, the player's hand returns to a fully known state
            player.pirates.filter( p => p.state == PirateState.Hand ).map ( p =>
                p.known = true
            )
        }

        return RetriableMethodResponse.Complete
    }
    def getSubRank(player : Player) : Int = {
        return Array(2, 1, 4, 3, 5, 6)(player.playerId);
    }
}