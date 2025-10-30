package com.example.memoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoard extends AppCompatActivity implements OnCardFlipListener {

    private final List<MemoryCard> flippedCards = new ArrayList<>();
    private int pairMatched = 0;
    private GameLogic gameLogic;
    private final DelayExecutor executor = (task, delay) -> new Handler().postDelayed(task, delay);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_board);

        Navigator navigator = new Navigator(this);
        Button endGameButton = findViewById(R.id.endGameButton);
        endGameButton.setOnClickListener(v -> navigator.goToMenu());

        // Tworzenie kart
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        LayoutInflater inflater = LayoutInflater.from(this);
        CardFactory factory = new CardFactory(inflater, gridLayout, this);
        List<MemoryCard> cards = factory.createCards();


        String difficultyName = getIntent().getStringExtra("difficulty");
        DifficultyLevel currentLevel = difficultyName != null
                ? DifficultyLevel.valueOf(difficultyName)
                : DifficultyLevel.EASY;


        // Wyświetlanie ilości szans
        LinearLayout linearLayout = findViewById(R.id.lifeBar);
        ChanceFactory chanceFactory = new ChanceFactory(currentLevel.getLives(),inflater,linearLayout,this);
        List<ImageView> lifeIcons = chanceFactory.printChances();
        // logika gry
        gameLogic = new GameLogic(this::showModal, executor, lifeIcons,currentLevel);


    }

    // prywatna metoda ukazująca modal końca etapu
    private void showModal(boolean isWin) {
        FrameLayout modal = findViewById(R.id.endingGameModal);
        TextView resultText = findViewById(R.id.gameResultText);
        Button nextGameButton = findViewById(R.id.nextGameButton);
        Button endGameButton = findViewById(R.id.endGameButton);

        resultText.setText(isWin ? "Wygrałeś!" : "Koniec Gry");
        modal.setVisibility(View.VISIBLE);

        if(isWin){
            nextGameButton.setEnabled(true);
            nextGameButton.setOnClickListener( v ->{
                DifficultyLevel nextLevel = gameLogic.getLives().next();
                Intent intent = new Intent(GameBoard.this, GameBoard.class);
                intent.putExtra("difficulty",nextLevel.name());
                startActivity(intent);
                finish();
            });
        }else {
            nextGameButton.setEnabled(false);
        }

    }


    @Override
    public void onCardFlip(MemoryCard card) {
        gameLogic.handleFlip(card);
    }

}
