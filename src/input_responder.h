#pragma once

#include <vector>

#include "input_request.h"

using namespace std;

class InputResponder {
public:
    // TODO: Pluralize?
    virtual vector<InputRequest> getInputRequests(int playerId);
    virtual void responseToInputRequest(int playerId);
};