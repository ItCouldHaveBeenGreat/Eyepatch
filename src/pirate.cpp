#include "pirate.h"

Pirate::Pirate(const Player& owner) {
    owningPlayer = &owner;
    subRank = getSubRankFromPlayerId(owner.getId());
}

Player Pirate::getOwner() {
    return *owningPlayer;
}
