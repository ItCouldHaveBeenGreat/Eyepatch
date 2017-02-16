#pragma once

#include <vector>
#include <map>

#include "input_request.h"

using namespace std;

class InputManager {
public:
    static InputManager instance() {
        if (instancePointer == nullptr) {
            instancePointer = new InputManager();
        }
        return *instancePointer;
    }

    // TODO: idempotent
    // TODO: move to input manager
    void addInputRequest(InputRequest &request);
    
    InputRequest getInputRequest(InputRequest &request);
    bool respondToInputRequest(int playerId, int response);
    
    void resolveInputRequest();


private:
    static InputManager* instancePointer;
};