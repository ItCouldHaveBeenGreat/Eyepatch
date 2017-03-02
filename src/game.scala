class Game(numPlayers : Int) {
    var currentVoyage: Voyage = new Voyage()
    var voyagesTaken : Int = 0
    val totalVoyages : Int = 3
    
    PlayerManager.build(numPlayers)
    
    def makeProgress() : RetriableMethodResponse.Value = {
        var response = currentVoyage.makeProgress()
        if (response == RetriableMethodResponse.Complete) {
            println("Voyage " + voyagesTaken + " complete")
            currentVoyage.endVoyage()
            voyagesTaken += 1
            if (voyagesTaken >= totalVoyages) {
                println("Game complete")
                return RetriableMethodResponse.Complete
            } else {
                currentVoyage = new Voyage()
                return RetriableMethodResponse.MadeProgress
            }
        } else {
            // Pass response up if PendingInput or MadeProgress
            return response
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