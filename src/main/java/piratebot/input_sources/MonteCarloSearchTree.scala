package main.java.piratebot.input_sources

import com.rits.cloning.Cloner
import main.java.piratebot.InputRequestType.InputRequestType
import main.java.piratebot.{Game, InputRequestType, RetriableMethodResponse}

import scala.concurrent.duration._
import scala.util.Random

// does this reeeallly need to take a player as input? Arguably we can just calculate this by looking at the input request needing an answer
// then again, we always want to do this because we're building this tree to inform a specific input request (with a specific player)
class MonteCarloSearchTree(rootGame: Game,
                           playerMakingChoice: Int,
                           choices: List[Int],
                           choiceType: InputRequestType.Value,
                           rolloutFunction: (InputRequestType, List[Int], Seq[Int]) => Int = Rollout.random_rollout) {
    private val cloner = new Cloner

    val rootNode = new MonteCarloNode()
    rootNode.playerMakingChoice = playerMakingChoice
    rootNode.choiceType = choiceType
    rootNode.initializeChildren(choices)

    def playOut(playerId: Int, timeoutInMillis: Int) : Unit = {
        val timer = timeoutInMillis.milliseconds.fromNow
        while (timer.hasTimeLeft()) {
            try {
                val clonedGame = cloner.deepClone(rootGame)
                // TODO: Move some of the cloning logic into the game object
                clonedGame.totalVoyages = 0 // terminates game after one voyage
                clonedGame.printer.silence() // prevents output spam
                clonedGame.inputManager.clearPendingRequests() // bot isn't allowed to know about these requests
                playOut(rootNode, clonedGame, playerId)
            } catch {
                case e: Exception => println(e)
            }
        }
    }

    // The quality of the best choice will depend on the amount of time spent doing playOuts
    def getBestChoice: Int = {
        rootNode.childStates.maxBy{ case(_, node) => node.wins.toDouble / node.plays}._1
    }

    // please refactor everything between the game, input source, and runner
    def playOut(currentNode: MonteCarloNode, game: Game, botPlayer: Int) : Boolean = {
        currentNode.plays += 1

        // advance game until we either complete or require input
        while (game.makeProgress() == RetriableMethodResponse.MadeProgress) { }
        if (game.makeProgress() == RetriableMethodResponse.Complete) {
            // if the game is complete, determine if our player has won or lost and return the result
            currentNode.wins += 1
            return botPlayer == game.playerManager.players.maxBy(p => p.doubloons).playerId
        }

        // traversal phase
        // oh god this code
        val choiceToMake =
        if (currentNode.childStates.isEmpty) {
            // we've reached the end of the tree, so begin a rollout
            val playerMakingChoice = 0.to(game.playerManager.numPlayers)
                .filter(playerNum => game.inputManager.getInputRequest(playerNum) != null && game.inputManager.getInputRequest(playerNum).answer.isEmpty) // null sucks
                .min

            val request = game.inputManager.getInputRequest(playerMakingChoice)
            //logger.debug("request_type: " + request.inputType + ", players" + request.playerId + ", choices" + request.choices)
            currentNode.initializeChildren(request.choices.values)
            currentNode.playerMakingChoice = request.playerId
            currentNode.choiceType = request.inputType

            rolloutFunction(request.inputType, request.choices.values.toList, game.getNormalizedGameState(request.playerId))

        } else {
            // otherwise, traverse the tree by selecting the next node with the MTCS algorithm
            val nextNodePair = currentNode.childStates.maxBy{case (_, b) => b.getConfidenceUpperBound(rootNode.plays)}
            nextNodePair._1
        }

        // answer the input request and continue traversal
        //logger.warn("Current node player " + currentNode.playerMakingChoice + ", " + currentNode.choiceType.toString + ", " + choiceToMake + ", " + currentNode.childStates)
        //logger.warn("Current input request " + game.inputManager.getInputRequest(currentNode.playerMakingChoice))
        //logger.warn("Current input request " + game.inputManager.getInputRequest(currentNode.playerMakingChoice).inputType.toString + ", " + game.inputManager.getInputRequest(currentNode.playerMakingChoice).choices)
        game.inputManager.answerInputRequest(currentNode.playerMakingChoice, choiceToMake)
        val isWin = playOut(currentNode.childStates(choiceToMake), game, botPlayer)
        if (isWin) {
            currentNode.wins += 1
        }
        isWin
    }


}

class MonteCarloNode() {
    var wins = 0
    var plays = 0
    var playerMakingChoice: Int = _
    var choiceType: InputRequestType.Value = _
    var childStates: Map[Int, MonteCarloNode] = Map()

    def initializeChildren(choices: Iterable[Int]): Unit = {
        childStates = choices.map(choice => (choice, new MonteCarloNode())).toMap
    }

    def getConfidenceUpperBound(totalPlays: Int): Double = {
        if (plays == 0) {
            Int.MaxValue
        } else {
            wins.toFloat / plays + Math.sqrt(2.0) * Math.sqrt(Math.log(totalPlays) / plays)
        }
    }
}

object Rollout {
    def random_rollout(requestType: InputRequestType, choices: List[Int], gameState: Seq[Int]): Int = {
        choices(Random.nextInt(choices.size))
    }
}

