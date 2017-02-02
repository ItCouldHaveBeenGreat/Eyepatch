#pragma once

#include <vector>

class InputResponder {
public:
    // TODO: Pluralize?
    virtual vector<InputRequest> getInputRequests(int playerId);
    virtual void responseToInputRequest(int playerId);
};