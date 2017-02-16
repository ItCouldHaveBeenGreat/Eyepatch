#pragma once

#include <vector>

#include "booty.h"
#include "player.h"
#include "retriable_method_response.h"
#include "round.h"

using namespace std;

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
