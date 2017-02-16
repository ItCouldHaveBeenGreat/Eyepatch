#pragma once

#include <cassert>
#include <vector>

#include "../pirate.h"

class GovernorsDaughter : public Pirate {
public:
    const string getName() { return "Governor's Daughter"; }
    const int getRank() { return 25; }

    // If no one else has a Governor's Daughter, +6 doubloons
    // Otherwise, -3 doubloons
    RetriableMethodResponse endOfVoyageAction() {
        Game g = Game::instance();
        if (isOnlyGovernorsDaughterInDen()) {
            g.getPlayer(getOwnerId()).setDoubloons(g.getPlayer(getOwnerId()).getDoubloons() + 6);
        } else {
            g.getPlayer(getOwnerId()).setDoubloons(g.getPlayer(getOwnerId()).getDoubloons() - 3);
        }
        return RetriableMethodResponse::Complete;
    }

private:
    bool isOnlyGovernorsDaughterInDen() {
        Game g = Game::instance();
        for (Player& p : g.getPlayers()) {
            // ignore self
            if (p == g.getPlayer(getOwnerId())) {
                continue;
            }
            bool found = p.getDen().end() == find_if(p.getDen().begin(), p.getDen().end(),
                [this](const Pirate& pirate) {
                    return pirate.getRank() == getRank();
            });
            return !found;
        }
    }

    vector<int> getSubRanks(int playerId) {
        return {4, 5, 3, 6, 1, 2};
    }
};