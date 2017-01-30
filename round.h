#pragma once

#include <algorithm>
#include <vector>

#include "booty.h"
#include "player.h"
#include "retriable_method_response.h"

#include "game.h"


using namespace std;

class Round {
public:
    Round(vector<Booty> &loot) : loot(loot) { } 


    /*
        Attempts to progress through the state machine of the round
    */
    RetriableMethodResponse attemptProgress();
    
    /*
        Cleans up the round, returning all pirates to dens and executing
        night time actions
    */
    void endRound();

private:
    vector<Booty> loot;
    vector<Pirate> pirates;
    vector<Pirate>::iterator dayIterator;
    vector<Pirate>::iterator duskIterator;
};