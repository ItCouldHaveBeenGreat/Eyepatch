#pragma once

#include "pirate.h"

class Carpenter : public Pirate {
public:
    const String getName() { return "Carpenter"; }
    const int getRank() { return 9; }
    
    // Lose 50% of doubloons
    RetriableMethodResponse dayAction() {
        Player p = *getOwningPlayer();
        p.setDoubloons(p.getDoubloons() / 2);
        
    }
  
    // Gain 10 doubloons
    RetriableMethodResponse endOfVoyageAction() {
        Player p = *getOwningPlayer();
        p.setDoubloons(p.getDoubloons() + 10);
    }
    
};

