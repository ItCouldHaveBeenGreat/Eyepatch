#pragma once

#include <vector>

#include "input_request.h"

using namespace std;

class InputSolicitor {
public:
    virtual void addInputRequest(int playerId, InputType type, vector<int> possibleValues);
    virtual InputRequest getInputRequest(int playerId, InputType type);
    virtual void completeInputRquest(int playerId, InputType type);
    
};