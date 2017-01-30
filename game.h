#pragma once

#include <vector>

#include "player.h"
#include "pirate.h"
#include "voyage.h"
#include "input_request.h"

using namespace std;

class Game {
public:
    static Game* getGameState() {
        if (instance == nullptr) {
            instance = new Game();
        }
        return instance;
    }


    
    // Move game state forward
    void attemptProgress() {
        
    }
    
    Voyage getActiveVoyage() { return activeVoyage; }
    Round getActiveRound() { return activeVoyage.getActiveRound(); }
    vector<Player> getPlayers() { return players; }

private:
    static Game* instance;
    vector<Player> players;
    
    int voyagesTaken = 0;
    Voyage activeVoyage;
    
    Game();
};