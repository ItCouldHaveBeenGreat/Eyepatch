#pragma once

#include <vector>

#include "player.h"
#include "retriable_method_response.h"
#include "voyage.h"

using namespace std;

class Game {
public:
    const static int NUMBER_OF_VOYAGES = 3;

    inline static Game instance() {
        if (!instancePtr) {
            throw new domain_error("Game hasn't been initialized!");
        }
        return *instancePtr;
    }

    inline static Game initialize(const int numPlayers) {
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
    
    inline Player getPlayer(int id) { return players[id]; }
    inline vector<Player> getPlayers() { return players; }

private:
    static Game* instancePtr;
    
    vector<Player> players;
    int voyagesTaken = 0;
    Voyage activeVoyage;
    
    Game();
    
    void dealPirates(int);
};