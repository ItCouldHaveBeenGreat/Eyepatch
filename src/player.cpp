#include "player.h"

//#include "pirates/governors_daughter.h"

Player::Player() {
    id = getNextPlayerId();
    this->constructDeck();
}

vector<Pirate> Player::getPiratesInState(const PirateState state) const {
    vector<Pirate> piratesToReturn = vector<Pirate>();
    for (const Pirate& p : deck) {
        if (p.getState() == state) {
            piratesToReturn.push_back(p);
        }
    }
    return piratesToReturn;
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

void Player::constructDeck() {
    //deck.push_back(GovernorsDaughter(getId()));
}
