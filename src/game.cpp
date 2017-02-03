#include "game.h"

RetriableMethodResponse Game::attemptProgress(bool runUntilBlocked) {
    do {
        const RetriableMethodResponse response = activeVoyage.attemptProgress();

        if (response == RetriableMethodResponse::PendingInput) {
            return response;
        } else if (response == RetriableMethodResponse::Complete) {
            // voyage complete; instaniate new voyage or end
            activeVoyage.endVoyage();
            voyagesTaken++;
            if (voyagesTaken < NUMBER_OF_VOYAGES) {
                dealPirates(Voyage::NUMBER_OF_ROUNDS);
                activeVoyage = Voyage(); 
            } else {
                return RetriableMethodResponse::Complete;
            }
            
        }
    } while (runUntilBlocked);
    
    // only reachable if runUntilBlocked is false
    return RetriableMethodResponse::MadeProgress;
}