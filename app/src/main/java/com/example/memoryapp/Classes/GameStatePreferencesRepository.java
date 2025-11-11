package com.example.memoryapp.Classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.memoryapp.Interfaces.IGameStateDeleter;
import com.example.memoryapp.Interfaces.IGameStateLoader;
import com.example.memoryapp.Interfaces.IGameStateSaver;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameStatePreferencesRepository implements IGameStateLoader, IGameStateSaver, IGameStateDeleter {

    private static final String PREF_NAME = "GameStates";
    private final SharedPreferences preferences;
    private final Gson gson = new Gson();

    public GameStatePreferencesRepository(Context context){
        this.preferences = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
    }

    @Override
    public void saveState(String username, GameState state){
        String json = gson.toJson(state);
        preferences.edit().putString(username,json).apply();
    }
    @Override
    public void deleteState(String username) {
        preferences.edit().remove(username).apply();

    }

    @Override
    public GameState loadState(String username) {
        String json = preferences.getString(username,null);
        if(json == null) return null;
        return gson.fromJson(json,GameState.class);
    }

    @Override
    public List<String> listSavedStates() {
        return new ArrayList<>(preferences.getAll().keySet());
    }

}
