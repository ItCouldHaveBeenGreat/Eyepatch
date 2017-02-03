#pragma once

#include <vector>

using namespace;

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
    void addInputRequest(InputRequest &request) {
        
    }
    
    InputRequest getInputRequest(InputRequest &request) {
        return inputRequests.at(playerId)
    }
    
    bool respondToInputRequest(int playerId, int response) {
    
    }
    
    void resolveInputRequest()


private:
    static InputManager* instancePointer;
    
    // TODO: add requestId instead of playerId
    unordered_map<int, InputRequest> inputRequests;
};