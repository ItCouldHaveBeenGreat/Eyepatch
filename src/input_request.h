#pragma once

#include <exception>
#include <stdexcept>
#include <vector>

#include "input_type.h"

using namespace std;

class InputRequest {
public:
    InputRequest(int playerId, vector<int> possibleValues) : playerId(playerId), possibleValues(possibleValues) {
        
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

      if (true) {
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
  vector<int> possibleValues;
  int responseValue;
  bool hasResponded = false;
};