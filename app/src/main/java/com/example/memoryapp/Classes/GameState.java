package com.example.memoryapp.Classes;

import com.example.memoryapp.Enum.DifficultyLevel;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {

    public List<CardState> cards;
    public int pairsMatched;
    public int remainingLives;
    public DifficultyLevel difficultyLevel;
    public boolean isChecked;


}
