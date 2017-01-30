#pragma once

#include "pirate.h"

class Carpenter : public Pirate {
public:
    const String getName() { return "Carpenter"; }
    const int getRank() { return 9; }
    
    // Lose 50% of doubloons
    virtual bool dayAction() {
        Player p = *getOwningPlayer();
        p.setDoubloons(p.getDoubloons() / 2);
        
    }
  
    // Gain 10 doubloons
    virtual bool endOfVoyageAction() {
        Player p = *getOwningPlayer();
        p.setDoubloons(p.getDoubloons() + 10);
    }
    
};

