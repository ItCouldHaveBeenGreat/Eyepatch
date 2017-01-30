

RetriableMethodResponse Round::attemptProgress() {
    // State: Round hasn't started
    if (!dayIterator) {
        // State: Soliciting pirate selection
        if (Game::getGameState()->getPlayers().size() <= pirates.size()) {
            // solicit choices from each player
            return RetriableMethodResponse::PendingInput;
        } else {
            // State: All players have submitted a pirate, sort them and set dayIterator
            sort(pirates.begin(), pirates.end());
            dayIterator = pirates.begin();
        }
    }
    
    // State: Iterating through day actions of pirates
    if (dayIterator != pirates.end()) {
        if (dayIterator->dayAction()) {
            dayIterator++;
            // State: Finished iterating over day actions, reverse vector and set duskIterator
            if (dayIterator == pirates.end()) {
                reverse(pirates.begin(), pirates.end());
                duskIterator = pirates.begin();
            }
        } else {
            return RetriableMethodResponse::PendingInput;
        }
    }
    
    // State: Iterating through dusk actions of pirates
    if (duskIterator != pirates.end()) {
        if (duskIterator->duskAction()) {
            duskIterator++;
        } else {
            return RetriableMethodResponse::PendingInput;
        }
    }

    // State: Finished iterating over dusk actions, return true; round is done
    if (duskIterator == pirates.end()) {
        return RetriableMethodResponse::Complete;
    }
    
    return RetriableMethodResponse::MadeProgress;
}

void Round::endRound() {
    // Move surviving pirates to den
    for (Pirate &p : pirates) {
        p.getOwningPlayer()->getDen().push_back(p);
    }
    
    // Do night time actions
    for (Player &p : getGameState()->getPlayers()) {
        p.doNightActions();
    }
}
