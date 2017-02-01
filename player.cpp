vector<Pirate> Player::getPiratesInState(const PirateState state) {
    vector<Pirate> piratesToReturn = vector<Pirate>();
    for (Pirate& p : deck) {
        if (p.getState() == state) {
            piratesToReturn.push_back(p);
        }
    }
    return pirates;
}