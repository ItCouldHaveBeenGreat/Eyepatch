#pragma once

#include <algorithm>
#include <vector>

#include "booty.h"
#include "pirate.h"
#include "pirate_state.h"
#include "round.h"

using namespace std;

class Player {
public:

    Player() {
        id = getNextPlayerId();
    }

    void doNightActions() {
        for (Pirate &p : getDen()) {
            p.nightAction();
        }
    }

    void doEndVoyageActions() {
        for (Pirate &p : getDen()) {
            p.endOfVoyageAction();
        }
    }

    void convertBootyIntoDoubloons();

    const vector<Booty> getLoot() const { return loot; }

    vector<Pirate> getDen() const { return getPiratesInState(PirateState::Den); }
    vector<Pirate> getHand() const  { return getPiratesInState(PirateState::Hand); }
    vector<Pirate> getDiscard() const { return getPiratesInState(PirateState::Discard); }

    vector<Pirate> getPiratesInState(const PirateState) const;

    int getId() const { return id; }
    int getDoubloons() const { return doubloons; }
    void setDoubloons(int howMany) { doubloons = max(0, howMany); }
    
    friend bool operator==(const Player& a, const Player& b) {
        return a.id == b.id;
    }
    
private:
    vector<Pirate> deck;
    vector<Booty> loot;
    
    int doubloons;
    int points;
    int id;
    
    static int getNextPlayerId() {
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
};