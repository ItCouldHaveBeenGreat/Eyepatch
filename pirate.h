#pragma once
 
#include <string>
#include <vector>

#include "player.h"
#include "pirate_state.h"
#include "retriable_method_response.h"

using namespace std;

class Player;

class Pirate {
public:
    Pirate(const Player&);

    virtual string getName() const;
    virtual int getRank() const;
    int getSubRank() const { return subRank; }

    virtual RetriableMethodResponse dayAction();
    virtual RetriableMethodResponse duskAction();
    virtual RetriableMethodResponse nightAction();
    virtual RetriableMethodResponse endOfVoyageAction();
    
    PirateState getState() const { return state; }
    Player getOwningPlayer();

    friend bool operator<(const Pirate& a, const Pirate& b) {
        if (a.getRank() == b.getRank()) {
            return a.getSubRank() > b.getSubRank();
        }
        return a.getRank() < b.getRank();
    }

private:
    Player* owningPlayer;
    PirateState state;
    int subRank;

    bool visible;
    bool unknown;

    void claimBooty();
    
    virtual int getSubRankFromPlayerId(int playerId);
};