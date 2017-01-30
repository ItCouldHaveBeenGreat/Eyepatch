#pragma once

#include <vector>

#include "input_type.h"

class InputRequest {
public:
    InputRequest(int playerId) : playerId(playerId) {
        
    }
private:
  int playerId;
  InputType type;
  vector<int> possibleValues;
};