package main.java.piratebot

import org.slf4j.LoggerFactory

abstract class Pirate(val game: Game, val player: Player) extends Ordered[Pirate] {
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
                // CASE: no booty to claim
                game.printer.print(Channel.Debug, "Player " + player.playerId + " had no booty to claim")
                return RetriableMethodResponse.Complete
            } else if (round.booty.size == 1) {
                // CASE: one booty to claim
                claimBooty(round.booty.head, round)
            } else {
                // CASE: choice of booty must be made
                val validAnswers = round.booty.map(b => b.toString -> b.id).toMap
                val request = game.inputManager.postOrGetInputRequest(player.playerId, InputRequestType.SelectBooty, validAnswers)
                if (request.answer.isEmpty) {
                    return RetriableMethodResponse.PendingInput
                } else {
                    val bootyToClaim = game.inputManager.getBootyFromInput(request)
                    game.inputManager.removeInputRequest(request.playerId)
                    claimBooty(bootyToClaim, round)
                }
            }
        }

        if (hasChosenSaber) {
            // CASE: saber was claimed and player must make an additional choice
            val choicesToKill = game.inputManager.getAdjacentDenPirates(player)
            if (choicesToKill.isEmpty) {
                // CASE: No one to kill
                RetriableMethodResponse.Complete
            } else if (choicesToKill.size == 1) {
                // CASE: Only one pirate to kill
                // TODO: this is so clunky
                val pirateToKill = game.playerManager
                    .getAdjacentPlayers(player)
                    .flatMap(p => p.pirates.filter(pirate => pirate.state == PirateState.Den))
                    .head
                saberPirate(pirateToKill)
                RetriableMethodResponse.Complete
            } else {
                // CASE: A choice of pirate to kill must be made
                val request = game.inputManager.postOrGetInputRequest(
                    player.playerId,
                    InputRequestType.KillPirateInAdjacentDen,
                    game.inputManager.getAdjacentDenPirates(player))
                if (request.answer.isEmpty) {
                    RetriableMethodResponse.PendingInput
                } else {
                    val target = game.inputManager.getTargetPirateFromInput(request)
                    saberPirate(target)
                    game.inputManager.removeInputRequest(request.playerId)
                    RetriableMethodResponse.Complete
                }
            }
        } else {
            RetriableMethodResponse.Complete
        }
    }

    def claimBooty(booty: Booty.Value, round: Round): Unit = {
        if (booty == Booty.Saber) {
            hasChosenSaber = true
        }

        if (booty == Booty.SpanishOfficer) {
            round.killPirate(this)
        }

        player.booty(booty) = player.booty(booty) + 1
        round.booty -= booty
        game.printer.print(Channel.Game, "Player " + player.playerId + " claims " + booty)
    }

    def saberPirate(targetPirate: Pirate): Unit = {
        targetPirate.state = PirateState.Discard
        game.printer.print(Channel.Game, tag + ": saber'ed " + targetPirate.tag)
        hasChosenSaber = false
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
    
    override def compare(that: Pirate) : Int = {
        if (rank == that.rank) {
            return this.subRank - that.subRank // subrank ascending
        }
        this.rank - that.rank // rank ascending
    }

    def tag : String = { name + "(" + player.getPlayerColor + ")" }
    
    protected def getSubRank(player : Player) : Int
}