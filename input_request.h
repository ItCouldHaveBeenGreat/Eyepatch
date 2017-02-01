#pragma once

#include <exception>
#include <set>

#include "input_type.h"

class InputRequest {
public:
    InputRequest(int playerId, set<int> possibleValues) : playerId(playerId), possibleValues(possibleValues) {
        
    }
    
    // Attempts to respond to the InputRequest; returns true if successful
    bool respond(int playerId, int responseValue) {
      if (hasResponded) {
        // you can only respond once
        return false;
      }
      if (playerId != this->playerId) {
        // wrong player
        // TODO: Actual Authentication
        return false;
      }

      if (possibleValues.find(responseValue) != possibleValues.end()) {
        this->responseValue = responseValue;
        hasResponded = true;
        return true;
      }
      return false;
    }
    
    bool getHasResponded() { return hasResponded; }
    bool getResponseValue() {
      if (!hasResponded) {
        throw domain_error("No responseValue has been set!");
      }
      return responseValue;
    }
    
    int getPlayerId() { return playerId; }
    
private:
  int playerId;
  InputType type;
  set<int> possibleValues;
  int responseValue;
  bool hasResponded = false;
};