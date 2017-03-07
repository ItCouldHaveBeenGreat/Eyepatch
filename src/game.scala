import scala.util.Random

class Game(numPlayers : Int) {
    PlayerManager.build(numPlayers)
    
    var currentVoyage: Voyage = new Voyage(numPlayers)
    var voyagesTaken : Int = 0
    val totalVoyages : Int = 1

    dealPirates(9)
    
    def makeProgress() : RetriableMethodResponse.Value = {
        var response = currentVoyage.makeProgress()
        if (response == RetriableMethodResponse.Complete) {
            println("Voyage " + voyagesTaken + " complete")
            voyagesTaken += 1
            if (voyagesTaken >= totalVoyages) {
                println("Game complete")
                return RetriableMethodResponse.Complete
            } else {
                dealPirates(6)
                currentVoyage = new Voyage(numPlayers)
                return RetriableMethodResponse.MadeProgress
            }
        } else {
            // Pass response up if PendingInput or MadeProgress
            return response
        }
    }
    
    def dealPirates(numPirates : Int) = {
        for (i <- 1 to numPirates) {
           val deck = PlayerManager.players.head.pirates.filter( p => p.state == PirateState.Deck)
           val pirateToDraw = deck(Random.nextInt(deck.size)).rank
           for (p <- PlayerManager.players) {
               // NOTE: Rank is 1 indexed
               p.dealPirate(pirateToDraw)
           }
           println("Game dealt pirate " + pirateToDraw)
        }
    }
    
    def getGameState : GameState = {
        return new GameState()
    }
}

// what needs to access the game?

// pirates need to access all players and pirates
// the saber needs to access all players and pirates
// pirates need to acess the round

// game state is composed of:
// the number of players
// the current voyage
//      the booty
// the current round
//      the booty
//      currently played pirates
// the players
//      pirates
//          den
//          hand
//          graveyard
//      booty
//      doubloons
//      points

// player: methods of access:
// GAME
//  -getState()
//  -makeProgress()
// INPUT MANAGER
//  -getInputRequest(playerId)
//  -answerInputRequest(inputRequestId, answer)


// pirate: methods of access
// ROUND
//  -getCurrentRound()
//      -getPirates

// pirates modify the state of other pirates

// PLAYERS
//  -getPlayers()
//  -getAdjacentPlayers()