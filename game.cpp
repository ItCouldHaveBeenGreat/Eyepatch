// FLOW
// 1. game needs input (pick a pirate)
// 2. game looks for the preferred input channel for the player (command line, neural network)
// 2.a maybe the input channel should just be REST? and have every entity spin up a webserver
// 2.b the rest module is still the right way to go
// 3. the game creates a session with this input channel
// 4. 


// GAME ---->     -> SOLICITER(s)
// LISTENER(s) --(call)--> INPUT MANAGER --(lookup)--> SESSION ---(?)---> GAME STATE + GAME TICK

// players waiting on
// voyage
//  -booty collections
// round within voyage
//  -pirates chosen
// iterator within round
//  -iterator is a combination of day/dusk and position 
// playerstate
//  -hand
//  -graveyard
//  -den
//  -doubloons
//  -points
//  -booty
// tick()
// cron to ping tick()
// asynchronous tick() on input change

void Game::killPirate(Pirate &p) {
    // throw if pirate is already dead, in hand, or doesn't exist
    // remove pirate from den, or board
    
    p.getOwningPlayer()->getDen()
}

void Game::deal(const int number) {
    
}