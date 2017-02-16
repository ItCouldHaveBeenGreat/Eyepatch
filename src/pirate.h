#pragma once
 
#include <string>
#include <vector>
#include <cassert>

#include "pirate_state.h"
#include "retriable_method_response.h"

using namespace std;

class Pirate {
public:
    Pirate(const int);

    virtual string getName() = 0 const;
    virtual int getRank() = 0 const;
    inline int getSubRank() const { return subRank; }
    inline PirateState getState() const { return state; }
    inline int getOwnerId() { return owningPlayerId; }

    virtual RetriableMethodResponse dayAction() = 0;
    virtual RetriableMethodResponse duskAction() = 0;
    virtual RetriableMethodResponse nightAction() = 0;
    virtual RetriableMethodResponse endOfVoyageAction() = 0;
    
    inline friend bool operator<(const Pirate& a, const Pirate& b) {
        if (a.getRank() == b.getRank()) {
            return a.getSubRank() > b.getSubRank();
        }
        return a.getRank() < b.getRank();
    }
private:
    int owningPlayerId;
    int subRank;

    PirateState state;
    bool visible;
    bool unknown;

    RetriableMethodResponse claimBooty();
    int getSubRankFromPlayerId(int playerId);
    virtual vector<int> getSubRanks() = 0;
};