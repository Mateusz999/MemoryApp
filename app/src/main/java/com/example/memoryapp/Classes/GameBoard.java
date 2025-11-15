package com.example.memoryapp.Classes;

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

import com.example.memoryapp.Enum.DifficultyLevel;
import com.example.memoryapp.Interfaces.DelayExecutor;
import com.example.memoryapp.Interfaces.OnCardFlipListener;
import com.example.memoryapp.Interfaces.OnGameStateLoaded;
import com.example.memoryapp.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends AppCompatActivity implements OnCardFlipListener {

    private final List<MemoryCard> flippedCards = new ArrayList<>();
    private int pairMatched = 0;
    private GameLogic gameLogic;
    private GameState currentState;
    private GameStatePreferencesRepository repository;
    private List<MemoryCard> cards;
    private final DelayExecutor executor = (task, delay) -> new Handler().postDelayed(task, delay);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentState = new GameState();
        repository = new GameStatePreferencesRepository(this);
        setContentView(R.layout.game_board);

        SwitchMaterial materialSwitch = findViewById(R.id.material_switch);
        boolean isChecked = materialSwitch.isChecked();


        Navigator navigator = new Navigator(this);
        Button endGameButton = findViewById(R.id.endGameButton);
        endGameButton.setOnClickListener(v -> navigator.goToMenu());

        // Tworzenie kart
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        LayoutInflater inflater = LayoutInflater.from(this);
        CardFactory factory = new CardFactory(inflater, gridLayout, this,repository);
         cards = factory.createCards();


        String difficultyName = getIntent().getStringExtra("difficulty");
        DifficultyLevel currentLevel = difficultyName != null
                ? DifficultyLevel.valueOf(difficultyName)
                : DifficultyLevel.EASY;


        // Wyświetlanie ilości szans
        LinearLayout linearLayout = findViewById(R.id.lifeBar);
        ChanceFactory chanceFactory = new ChanceFactory(currentLevel.getLives(),inflater,linearLayout,this);
        List<ImageView> lifeIcons = chanceFactory.printChances();
        // logika gry
        gameLogic = new GameLogic(this::showModal, executor, lifeIcons,currentLevel,isChecked,currentState);

        materialSwitch.setOnCheckedChangeListener((buttonView, isCheckedNow) -> {
            gameLogic.setChecked(isCheckedNow);
        });

        Button saveButton = findViewById(R.id.saveButton);
        Button loadButton = findViewById(R.id.loadButton);
        saveButton.setOnClickListener(v -> {
            currentState = buildCurrentGameState();
            SaveDialogFragment dialog = new SaveDialogFragment(repository, currentState);
            dialog.show(getSupportFragmentManager(), "saveDialog");
        });
        loadButton.setOnClickListener(v -> {
            LoadDialogFragment dialog = new LoadDialogFragment(repository, new OnGameStateLoaded() {
                @Override
                public void onLoad(GameState state) {
                    GameBoard.this.onLoad(state);
                }
            });
            dialog.show(getSupportFragmentManager(), "loadDialog");
        });
    }

    public GameState buildCurrentGameState() {

        GameState state = new GameState();

        // Poziom trudności
        state.difficultyLevel = gameLogic.getLives();

        // Czy przełącznik "znikające karty" jest aktywny
        SwitchMaterial materialSwitch = findViewById(R.id.material_switch);
        state.isChecked = materialSwitch.isChecked();

        // Liczba dopasowanych par
        state.pairsMatched = gameLogic.getPairsMatched();

        // Liczba pozostałych żyć
        state.remainingLives = gameLogic.getRemainingLives();

        // Stan wszystkich kart
        List<CardState> cardStates = new ArrayList<>();
        for (MemoryCard card : cards) {
            CardState cs = new CardState();
            cs.imageResId = card.getImageResId();      // ikona karty
            cs.isFlipped = card.getFlipedState();      // czy karta jest odwrócona
            cs.isMatched = card.isMatched();           // czy karta została dopasowana
            cardStates.add(cs);
        }
        state.cards = cardStates;

        // przypisanie do currentState, żeby zawsze był aktualny
        currentState = state;

        return state;
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
        currentState = buildCurrentGameState();
    }

    private void onLoad(GameState state) {
        currentState = state;

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();
        cards.clear();

        SwitchMaterial materialSwitch = findViewById(R.id.material_switch);
        materialSwitch.setChecked(state.isChecked);

        DifficultyLevel level = state.difficultyLevel;

        LinearLayout lifeBar = findViewById(R.id.lifeBar);
        lifeBar.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        ChanceFactory chanceFactory = new ChanceFactory(level.getLives(), inflater, lifeBar, this);
        List<ImageView> lifeIcons = chanceFactory.printChances();

        for (CardState cs : state.cards) {
            View cardView = inflater.inflate(R.layout.memory_card, gridLayout, false);
            gridLayout.addView(cardView);

            MemoryCard card = new MemoryCard(cardView, this, repository);

            card.setIcon(cs.imageResId);
            if (cs.isFlipped) card.setFlipped(true);
            card.setMatched(cs.isMatched);

            cards.add(card);
        }

        gameLogic = new GameLogic(this::showModal, executor, lifeIcons, level, state.isChecked,currentState);
        gameLogic.setPairsMatched(state.pairsMatched);
        gameLogic.setRemainingLives(state.remainingLives);
    }


}
