package com.example.memoryapp.Enum;

public enum DifficultyLevel {
    EASY(20),
    MEDIUM(15),
    HARD(10),
    EXTREME(5);


    private final int lives;


    DifficultyLevel(int lives) {
        this.lives = lives;
    }

    public int getLives(){
        return lives;
    }

    public DifficultyLevel next(){
        DifficultyLevel[] levels = values();

        int ordinal = this.ordinal();
        if(ordinal < levels.length -1 ) return levels[ordinal+1];
        else return this;
    }
}
