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
        if (doOtherGovernorsDaughtersExist()) {
            getOwningPlayer().setDoubloons(getOwningPlayer().getDoubloons() - 3);
        } else {
            getOwningPlayer().setDoubloons(getOwningPlayer().getDoubloons() + 6);
        }
        return RetriableMethodResponse::Complete;
    }

private:
    bool doOtherGovernorsDaughtersExist() {
        Game g = Game::instance();
        for (Player& p : g.getPlayers()) {
            // ignore self
            if (p == getOwningPlayer()) {
                continue;
            }
            bool found = p.getDen().end() == find_if(p.getDen().begin(), p.getDen().end(),
                [this](const Pirate& pirate) {
                    return pirate.getRank() == getRank();
            });
            return found;
        }
    }

    int getSubRankFromPlayerId(int playerId) {
        assert(playerId >= 0 && playerId < Game::instance().getPlayers().size());
        static vector<int> subRanksByPlayerId = {4, 5, 3, 6, 1, 2};
        return subRanksByPlayerId[playerId];
    }
};