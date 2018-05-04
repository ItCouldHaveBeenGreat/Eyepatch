package main.java.piratebot.input_sources

import com.rits.cloning.Cloner
import main.java.piratebot._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.util.Random

class MonteCarloBot() extends InputSource with Statistics {
    protected val logger = LoggerFactory.getLogger(classOf[MonteCarloBot])

    override def makeDecision(request: InputRequest, state: Seq[Int], game: Game) : Int = {
        logger.info("Beginning rollout for " + request.inputType.toString + ", " + request.playerId)
        val searchTree = new MonteCarloSearchTree(game, request.playerId, request.choices.values.toList)
        searchTree.playOut(request.playerId, 500)
        searchTree.getBestChoice
    }

    override def endGame(player: Player, players: Seq[Player]): Unit = {
        if (player.points == players.maxBy( p => p.points ).points) {
            addCounter("wins", 1)
        }
        addCounter("games", 1)
    }

    override def endSession(): Unit = {
        printCounters
    }
}

class MonteCarloNode() {
    var wins = 0
    var plays = 0
    var playerMakingChoice = 0
    var childStates: Map[Int, MonteCarloNode] = Map()

    def initializeChildren(choices: Iterable[Int]): Unit = {
        childStates = choices.map(choice => (choice, new MonteCarloNode())).toMap
    }

    def getConfidenceUpperBound(totalPlays: Int): Double = {
        wins.toFloat/plays + Math.sqrt(2.0 * Math.log(totalPlays) / plays)
    }
}


// does this reeeallly need to take a player as input? Arguably we can just calculate this by looking at the input request needing an answer
// then again, we always want to do this because we're building this tree to inform a specific input request (with a specific player)
class MonteCarloSearchTree(rootGame: Game, playerMakingChoice: Int, choices: List[Int]) {
    protected val logger = LoggerFactory.getLogger(classOf[MonteCarloBot])

    private val cloner = new Cloner

    private val rootNode = new MonteCarloNode()
    rootNode.playerMakingChoice = playerMakingChoice
    rootNode.initializeChildren(choices)

    def playOut(playerId: Int, timeoutInMillis: Int) : Unit = {
        val timer = timeoutInMillis.milliseconds.fromNow
        while (timer.hasTimeLeft()) {
            val clonedGame = cloner.deepClone(rootGame)
            playOut(rootNode, clonedGame, playerId)
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

        // restructure to...
        // get to a pending input state
        // if the current node has no children, initialize the node by selecting a player and setting up the choices, then pick randomly (define this separately as a rollout method!!!)
        // if the current node has children, traverse using MTCS algorithm


        // traversal phase
        // oh god this code
        val choiceToMake =
        if (currentNode.childStates.isEmpty) {
            // we've reached the end of the tree, so begin a rollout
            val playerMakingChoice = 0.to(game.playerManager.numPlayers)
                .filter(playerNum => game.inputManager.getInputRequest(playerNum) != null && game.inputManager.getInputRequest(playerNum).answer.isEmpty) // null sucks
                .head

            val request = game.inputManager.getInputRequest(playerMakingChoice)
            logger.debug("choices" + request.choices)
            currentNode.initializeChildren(request.choices.values)
            currentNode.playerMakingChoice = request.playerId

            rollout(request.choices.values.toList)

        } else {
            // otherwise, traverse the tree by selecting the next node with the MTCS algorithm
            val nextNodePair = currentNode.childStates.maxBy{case (_, b) => b.getConfidenceUpperBound(rootNode.plays)}
            nextNodePair._1
        }

        // answer the input request and continue traversal
        logger.debug("Current node player " + currentNode.playerMakingChoice + ", " + choiceToMake + ", " + currentNode.childStates)
        game.inputManager.answerInputRequest(currentNode.playerMakingChoice, choiceToMake)
        val isWin = playOut(currentNode.childStates(choiceToMake), game, botPlayer)
        if (isWin) {
            currentNode.wins += 1
        }
        isWin
    }

    def rollout(choices: List[Int]): Int = {
        // right now we do random rollouts
        choices(Random.nextInt(choices.size))
    }


}