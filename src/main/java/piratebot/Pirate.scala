package main.java.piratebot

import org.slf4j.LoggerFactory

abstract class Pirate(val game: Game, val player: Player) extends Ordered[Pirate] {
    protected val logger = LoggerFactory.getLogger(classOf[Pirate])

    val rank : Int
    val name : String
    var known : Boolean = true
    val subRank: Int = getSubRank(player)

    var state : PirateState.Value = PirateState.Deck
    var hasChosenSaber : Boolean = false
    
    // By default, a pirate only claims booty
    def dayAction(round : Round) : RetriableMethodResponse.Value = {
        RetriableMethodResponse.Complete
    }
    def duskAction(round : Round) : RetriableMethodResponse.Value = {
        claimBooty(round)
    }
    def nightAction : RetriableMethodResponse.Value = {
        RetriableMethodResponse.Complete
    }
    def endOfVoyageAction() : Unit = { }
    

    def claimBooty(round : Round) : RetriableMethodResponse.Value = {
        if (!hasChosenSaber) {
            if (round.booty.isEmpty) {
                logger.debug("Player " + player.playerId + " had no booty to claim")
                return RetriableMethodResponse.Complete
            }
            val validAnswers = round.booty.map(b => b.toString -> b.id).toMap
            val request = game.inputManager.postAndGetInputRequest(player.playerId, InputRequestType.SelectBooty, validAnswers)
            if (request.answer.isEmpty) {
                return RetriableMethodResponse.PendingInput
            } else {
                val b = game.inputManager.getBootyFromInput(request)
                game.inputManager.removeInputRequest(request.playerId)
                
                if (b == Booty.Saber) {
                    hasChosenSaber = true
                }
    
                if (b == Booty.SpanishOfficer) {
                    round.killPirate(this)
                }
                
                player.booty(b) = player.booty(b) + 1
                round.booty -= b
                logger.info("Player " + player.playerId + " claims " + b)
            }
        }
        
        if (hasChosenSaber) {
            if (game.inputManager.getAdjacentDenPirates(player).nonEmpty) {
                val request = game.inputManager.postAndGetInputRequest(
                    player.playerId,
                    InputRequestType.KillPirateInAdjacentDen,
                    game.inputManager.getAdjacentDenPirates(player))
                if (request.answer.isEmpty) {
                    return RetriableMethodResponse.PendingInput
                } else {
                    val target = game.inputManager.getTargetPirateFromInput(request)
                    target.state = PirateState.Discard
                    logger.info(tag + ": sabered " + target.tag)
                    hasChosenSaber = false
                    game.inputManager.removeInputRequest(request.playerId)
                }
            }
        }

        RetriableMethodResponse.Complete
    }
    
    def publicState : PublicPirateState.Value = {
        if (!known) {
            PublicPirateState.Unknown
        } else {
            state match {
                case PirateState.Board => PublicPirateState.Board
                case PirateState.Deck => PublicPirateState.OutOfPlay
                case PirateState.Den => PublicPirateState.Den
                case PirateState.Discard => PublicPirateState.Discard
                case PirateState.Hand => PublicPirateState.Hand
                case PirateState.OutOfPlay => PublicPirateState.OutOfPlay
            }
        }
    }
    
    def compare(that: Pirate) : Int = {
        if (rank == that.rank) {
            return subRank - this.subRank
        }
        rank - that.rank
    }

    def tag : String = { name + "(" + player.playerId + ")" }
    
    protected def getSubRank(player : Player) : Int
}