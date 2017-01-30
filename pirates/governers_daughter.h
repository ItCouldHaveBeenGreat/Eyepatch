#pragma once

#include "pirate.h"

class GovernorsDaughter : public Pirate {
public:
    const String getName() { return "Governor's Daughter" }
    const int getRank() { return 25; }

    // If no one else has a Governor's Daughter, +6 doubloons
    // Otherwise, -3 doubloons
    virtual bool endOfVoyageAction() {
        Game g = getGameState()
        // ignore self
        for (Player p : g->getPlayers()) {
            if (p == &getOwningPlayer()) {
                continue;
            }
            
        }
        getOwningPlayer()->setDoubloons(getOwningPlayer()->getDoubloons() + 6);
    }

private:
    const bool isOnlyGovernorsDaughter() {
        
    }
};