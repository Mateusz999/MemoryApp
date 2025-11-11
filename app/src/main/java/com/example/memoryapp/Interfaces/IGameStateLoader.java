package com.example.memoryapp.Interfaces;

import com.example.memoryapp.Classes.GameState;

import java.util.List;

public interface IGameStateLoader {

    GameState loadState(String username);
    List<String> listSavedStates();
}
