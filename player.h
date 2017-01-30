#pragma once

#include <vector>

#include "booty.h"
#include "pirate.h"
#include "round.h"

using namespace std;

// TODO: Number to color mapping

class Player {
public:

    Player(int id) : id(id) { }

    void playPirate(Pirate &p, Round &r) {
        // add pirate to round
        // remove pirate from hand
        //  if the pirate is in hand and the round, it should only be in round
        
        // player initiated action
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

    void convertBootyIntoDoubloons() {
        
    }

    vector<Pirate> getDen() { return den; }
    vector<Pirate> getGraveyard() { return graveyard; }
    vector<Pirate> getHand() { return hand; }
    
    int getDoubloons() { return doubloons; }
    void setDoubloons(int howMany) { doubloons = howMany; }
    
private:
    vector<Pirate> den;
    vector<Pirate> graveyard;
    vector<Pirate> hand;
    vector<Booty> booty;
    
    int doubloons;
    int points;
    int id;
};