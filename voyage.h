#pragma once

#include "booty.h"
#include "game.h"

class Voyage {
    
public:
    const static int NUMBER_OF_ROUNDS = 6;

    Voyage();
    
    RetriableMethodResponse attemptProgress();
    void endVoyage();
    
    Round getActiveRound() { return activeRound; }
    
private:
    Round activeRound;
    int roundNumber;
    
    vector<vector<Booty>> bootyCollections;
};
