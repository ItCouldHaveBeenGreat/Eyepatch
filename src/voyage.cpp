#include "voyage.h"

RetriableMethodResponse Voyage::attemptProgress() {
    RetriableMethodResponse response = getActiveRound().attemptProgress();
    if (response == RetriableMethodResponse::PendingInput || response == RetriableMethodResponse::MadeProgress) {
        return response;
    } else if (response == RetriableMethodResponse::Complete) {
        getActiveRound().endRound();
        roundNumber++;
        if (roundNumber >= NUMBER_OF_ROUNDS) {
            return RetriableMethodResponse::Complete;
        } else {
            activeRound = Round(bootyCollections[roundNumber]);
            return RetriableMethodResponse::MadeProgress;
        }
    }
    return RetriableMethodResponse::MadeProgress;
}

void Voyage::endVoyage() {
    for (Player &p : Game::instance().getPlayers()) {
        p.doEndVoyageActions();
    }
    
    // record doubloons
}