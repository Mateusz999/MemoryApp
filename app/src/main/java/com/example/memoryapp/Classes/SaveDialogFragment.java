package com.example.memoryapp.Classes;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.memoryapp.Interfaces.IGameStateSaver;

public class SaveDialogFragment extends DialogFragment {
    private IGameStateSaver saver;
    private GameState currentState;

    public SaveDialogFragment(IGameStateSaver saver, GameState state) {
        this.saver = saver;
        this.currentState = state;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        EditText input = new EditText(getContext());
        input.setHint("Wpisz nazwę zapisu");

        return new AlertDialog.Builder(getActivity())
                .setTitle("Zapisz grę")
                .setView(input)
                .setPositiveButton("Zapisz", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        saver.saveState(name, currentState);
                    }
                })
                .setNegativeButton("Anuluj", null)
                .create();
    }
}
