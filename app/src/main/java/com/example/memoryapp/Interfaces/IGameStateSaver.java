package com.example.memoryapp.Interfaces;

import com.example.memoryapp.Classes.GameState;

public interface IGameStateSaver {

    public void saveState(String username, GameState state);
}
