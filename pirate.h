#pragma once
 
#include <string>

#include "player.h"

class Player;

using namespace std;

class Pirate {
public:
    virtual string getName() const;
    virtual int getRank() const;

    virtual bool dayAction();
    virtual bool duskAction();
    virtual bool nightAction();
    virtual bool endOfVoyageAction();
    
    int getSubRank() const { return subRank; }
    Player* getOwningPlayer() { return owningPlayer; }

    friend bool operator<(const Pirate& a, const Pirate& b) {
        if (a.getRank() == b.getRank()) {
            return a.getSubRank() > b.getSubRank();
        }
        return a.getRank() < b.getRank();
    }

private:
    Player* owningPlayer;
    int subRank;
    bool visible;
    bool unknown;

    void claimBooty();
};