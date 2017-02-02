#pragma once

#include <vector>

#include "player.h"
#include "pirate.h"
#include "voyage.h"
#include "input_request.h"
#include "retriable_method_response.h"

using namespace std;

class Game {
public:
    const static int NUMBER_OF_VOYAGES = 3;

    static Game instance() {
        if (!instancePtr) {
            throw new domain_error("Game hasn't been initialized!");
        }
        return *instancePtr;
    }

    static Game initialize(const int numPlayers) {
        if (instancePtr) {
            throw new domain_error("Game has already been initialized!");
        }

        instancePtr = new Game();
        for (int i = 0; i < numPlayers; i++) {
            instancePtr->players.emplace_back();
        }
        return *instancePtr;
    }

    /*
    InputSolicitor getInputSolicitor() { return inputManager; }
    InputResponder getInputResponder() { return inputManager; }
    */
  
    
    RetriableMethodResponse attemptProgress(bool runUntilBlocked);
    
    Voyage getActiveVoyage() { return activeVoyage; }
    Round getActiveRound() { return activeVoyage.getActiveRound(); }
    vector<Player> getPlayers() { return players; }

private:
    static Game* instancePtr;
    
    vector<Player> players;
    int voyagesTaken = 0;
    Voyage activeVoyage;
    
    void dealPirates(int numberToDeal) {
        
    }
    
    Game();
};