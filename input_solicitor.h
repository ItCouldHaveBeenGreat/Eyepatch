#pragma once

class InputSolicitor {
public:
    virtual void addInputRequest(int playerId, InputType type, set<int> possibleValues);
    virtual InputRequest getInputRequest(int playerId, InputType type);
    virtual void completeInputRquest(int playerId, InputType type);
    
};