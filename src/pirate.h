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

    virtual RetriableMethodResponse dayAction() { return RetriableMethodResponse::Complete; }
    virtual RetriableMethodResponse duskAction() { return claimBooty(); }
    virtual RetriableMethodResponse nightAction() { return RetriableMethodResponse::Complete; }
    virtual RetriableMethodResponse endOfVoyageAction() { return RetriableMethodResponse::Complete; }
    
    PirateState getState() const { return state; }
    Player getOwner();

    friend bool operator<(const Pirate& a, const Pirate& b) {
        if (a.getRank() == b.getRank()) {
            return a.getSubRank() > b.getSubRank();
        }
        return a.getRank() < b.getRank();
    }

private:
    const Player* owningPlayer;
    const int subRank;

    PirateState state;
    bool visible;
    bool unknown;

    RetriableMethodResponse claimBooty();
    virtual int getSubRankFromPlayerId(int playerId);
};