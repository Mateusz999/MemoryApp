package com.example.memoryapp.Classes;

import android.view.View;
import android.widget.ImageView;

import com.example.memoryapp.Enum.DifficultyLevel;
import com.example.memoryapp.Interfaces.DelayExecutor;
import com.example.memoryapp.Interfaces.GameEndCallback;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private final List<MemoryCard> flippedCards = new ArrayList<>();
    private List<ImageView> chances = new ArrayList<>();
    private int pairMatched = 0;
    private final DelayExecutor delayExecutor;
    private final GameEndCallback onGameEnd;
    private boolean isChecked;
    private DifficultyLevel level;
    private boolean isBusy = false;

    public GameLogic(GameEndCallback onGameEnd, DelayExecutor delayExecutor, List<ImageView> chances, DifficultyLevel level, boolean isChecked) {
        this.onGameEnd = onGameEnd;
        this.delayExecutor = delayExecutor;
        this.chances = chances;
        this.level = level;
        this.isChecked = isChecked;
    }

    public void setPairsMatched(int count) {
        this.pairMatched = count;
    }

    public void setRemainingLives(int lives) {
        while (chances.size() > lives) {
            ImageView last = chances.remove(chances.size() - 1);
            last.setVisibility(View.INVISIBLE);
        }
    }

    public int getRemainingLives(){
        return chances.size();
    }
    public int getPairsMatched(){
        return pairMatched;
    }
    public DifficultyLevel getLives(){
        return level;
    }
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void handleFlip(MemoryCard card) {
        if (isBusy || flippedCards.contains(card)) return;

        if (flippedCards.size() < 2) {
            card.forcedFlip();
            flippedCards.add(card);
        }

        if (flippedCards.size() == 2) {
            isBusy = true;

            MemoryCard first = flippedCards.get(0);
            MemoryCard second = flippedCards.get(1);

            boolean isMatch = first.icon.getDrawable().getConstantState().equals(
                    second.icon.getDrawable().getConstantState());

            if (isMatch) {
                delayExecutor.postDelayed(() -> {
                    if (isChecked) {
                        first.flip();
                        second.flip();
                    }
                }, 500);

                pairMatched++;

                delayExecutor.postDelayed(() -> {
                    if (isChecked) {
                        first.hideCard();
                        second.hideCard();
                    }

                    if (pairMatched == 8) {
                        delayExecutor.postDelayed(() -> onGameEnd.onGameEnd(true), 1000);
                    }

                    isBusy = false;
                }, 1500);

            } else {
                delayExecutor.postDelayed(() -> {
                    first.flip();
                    second.flip();

                    if (!chances.isEmpty()) {
                        ImageView last = chances.get(chances.size() - 1);
                        last.setVisibility(View.INVISIBLE);
                        chances.remove(chances.size() - 1);
                    }

                    if (chances.size() < 1) {
                        delayExecutor.postDelayed(() -> onGameEnd.onGameEnd(false), 1000);
                    }

                    isBusy = false;
                }, 1000);
            }

            flippedCards.clear();
        }
    }

}
