#pragma once

#include <algorithm>
#include <vector>

#include "booty.h"
#include "pirate.h"

using namespace std;

class Player {
public:

    Player();

    void constructDeck();

    void doNightActions();
    void doEndVoyageActions();
    void convertBootyIntoDoubloons();

    inline vector<Booty> getLoot() const { return loot; }

    inline vector<Pirate> getDen() { return getPiratesInState(PirateState::Den); }
    inline vector<Pirate> getHand()  { return getPiratesInState(PirateState::Hand); }
    inline vector<Pirate> getDiscard() { return getPiratesInState(PirateState::Discard); }

    vector<Pirate> getPiratesInState(const PirateState) const;

    inline int getId() const { return id; }
    inline int getDoubloons() const { return doubloons; }
    inline void setDoubloons(int howMany) { doubloons = max(0, howMany); }
    
    inline friend bool operator==(const Player& a, const Player& b) {
        return a.id == b.id;
    }
    
    
    
private:
    
    
    inline static int getNextPlayerId() {
        // post-increment returns the original value
        // 0 = red
        // 1 = blue
        // 2 = yellow
        // 3 = green
        // 4 = black
        // 5 = white
        static int nextPlayerId = 0;
        return nextPlayerId++;
    }

    vector<Pirate> deck;
    vector<Booty> loot;
    
    int doubloons;
    int points;
    int id;
    
    
};