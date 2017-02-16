#include "round.h"

RetriableMethodResponse Round::attemptProgress() {
    // State: Soliciting pirate selection
    if (pirates.size() < Game::instance().getPlayers().size()) {
        // solicit choices from each player
        if (pirates.size() == Game::instance().getPlayers().size()) {
            // State: All players have submitted a pirate, sort them and set dayIterator
            sort(pirates.begin(), pirates.end());
            dayIterator = pirates.begin();
            return RetriableMethodResponse::MadeProgress;
        } else {
            return RetriableMethodResponse::PendingInput;
        }
    }
    
    // State: Iterating through day actions of pirates
    if (dayIterator != pirates.end()) {
        RetriableMethodResponse response = dayIterator->dayAction();
        if (response == RetriableMethodResponse::Complete) {
            dayIterator++;
            // State: Finished iterating over day actions, reverse vector and set duskIterator
            if (dayIterator == pirates.end()) {
                reverse(pirates.begin(), pirates.end());
                duskIterator = pirates.begin();
                return RetriableMethodResponse::MadeProgress;
            }
        } else {
            return response;
        }
    }
    
    // State: Iterating through dusk actions of pirates
    if (duskIterator != pirates.end()) {
        RetriableMethodResponse response = duskIterator->duskAction();
        if (response == RetriableMethodResponse::Complete) {
            duskIterator++;
        } else {
            return response;
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
        
        // TOOD: fix me!
        Game::instance().getPlayer(p.getOwnerId()).getDen();
    }
    
    // Do night time actions
    for (Player &p : Game::instance().getPlayers()) {
        p.doNightActions();
    }
}
