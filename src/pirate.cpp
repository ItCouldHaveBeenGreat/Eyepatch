#include "pirate.h"

Pirate::Pirate(const int playerId) : owningPlayerId(playerId), subRank(getSubRankFromPlayerId(playerId)) {

}

RetriableMethodResponse Pirate::dayAction() {
    return RetriableMethodResponse::Complete;
    
}

RetriableMethodResponse Pirate::duskAction() {
    return claimBooty();
    
}

RetriableMethodResponse Pirate::nightAction() {
    return RetriableMethodResponse::Complete;
    
}

RetriableMethodResponse Pirate::endOfVoyageAction() {
    return RetriableMethodResponse::Complete;
    
}

int Pirate::getSubRankFromPlayerId(int playerId) {
    // TODO: validation
    return getSubRanks()[playerId];
}