package com.example.memoryapp.Classes;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.memoryapp.Interfaces.IGameStateLoader;
import com.example.memoryapp.Interfaces.OnGameStateLoaded;

import java.util.List;
import java.util.function.Consumer;

public class LoadDialogFragment extends DialogFragment {
    private IGameStateLoader loader;
    private OnGameStateLoaded onLoad;

    public LoadDialogFragment(IGameStateLoader loader, OnGameStateLoaded onLoad) {
        this.loader = loader;
        this.onLoad = onLoad;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> saves = loader.listSavedStates();
        String[] items = saves.toArray(new String[0]);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Wczytaj zapis")
                .setItems(items, (dialog, which) -> {
                    String selected = items[which];
                    GameState state = loader.loadState(selected);
                    if (state != null) {
                        onLoad.onLoad(state);
                    }
                })
                .setNegativeButton("Anuluj", null)
                .create();
    }
}
