#include "pirate.h"

Pirate::Pirate(const Player& owner) {
    owningPlayer = &owner;
    subrank = getSubRankFromPlayerId(owner.getId())
}

Player Pirate::getOwner() {
    return *owningPlayer;
}
