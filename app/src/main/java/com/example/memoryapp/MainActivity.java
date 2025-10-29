package com.example.memoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // przypisujemy do zmiennej playbutton nasz obiekt przycisku

        Button playButton = findViewById(R.id.startGameButton);
        // ustawiamy do naszej zmiennej nasłuchiwacza zdarzeń
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
             // nadpisujemy funkcje onClick -> po naciśnięciu odtwieramy nową aktywność w tym wypadku jest to game_board
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameBoard.class);
                startActivity(intent);
            }
        });
    }
}