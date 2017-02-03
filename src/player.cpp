#include "player.h"

#include "pirates/governors_daughter.h"

Player::Player() {
    id = getNextPlayerId();
    constructDeck();
}

vector<Pirate> Player::getPiratesInState(const PirateState state) {
    vector<Pirate> piratesToReturn = vector<Pirate>();
    for (Pirate& p : deck) {
        if (p.getState() == state) {
            piratesToReturn.push_back(p);
        }
    }
    return pirates;
}

void Player::doNightActions() {
    for (Pirate &p : getDen()) {
        p.nightAction();
    }
}

void Player::doEndVoyageActions() {
    for (Pirate &p : getDen()) {
        p.endOfVoyageAction();
    }
}

private constructDeck() {
    deck.push_back(GovernorsDaughter(this));
}