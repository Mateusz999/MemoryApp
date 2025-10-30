package com.example.memoryapp;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GameLogic {
    private final List<MemoryCard> flippedCards = new ArrayList<>();
    private List<ImageView> chances = new ArrayList<>();
    private int pairMatched = 0;
    private final DelayExecutor delayExecutor;
    private final GameEndCallback onGameEnd;
    private DifficultyLevel level;
    public GameLogic(GameEndCallback onGameEnd, DelayExecutor delayExecutor, List<ImageView> chances,DifficultyLevel level) {
        this.onGameEnd = onGameEnd;
        this.delayExecutor = delayExecutor;
        this.chances = chances;
        this.level = level;
    }

    public DifficultyLevel getLives(){
        return level;
    }
    public void handleFlip(MemoryCard card) {
        if (flippedCards.size() < 2) {
            card.forcedFlip();
            if (!flippedCards.contains(card)) flippedCards.add(card);
        }

        if (flippedCards.size() == 2) {
            MemoryCard first = flippedCards.get(0);
            MemoryCard second = flippedCards.get(1);

            if (first.icon.getDrawable().getConstantState().equals(
                    second.icon.getDrawable().getConstantState())) {

                delayExecutor.postDelayed(() -> {
                    first.flip();
                    second.flip();
                }, 500);

                pairMatched++;

                delayExecutor.postDelayed(() -> {
                    first.hideCard();
                    second.hideCard();
                    if (pairMatched == 8) {
                        delayExecutor.postDelayed(() -> onGameEnd.onGameEnd(true), 1000);
                    }
                }, 1500);

            } else {
                delayExecutor.postDelayed(() -> {
                    first.flip();
                    second.flip();

                    if (!chances.isEmpty()) {
                        ImageView last = chances.get(chances.size() - 1);
                        last.setVisibility(View.INVISIBLE); // lub .setAlpha(0f) dla animacji
                        chances.remove(chances.size() - 1);
                    }

                    if(chances.size() < 1){
                        delayExecutor.postDelayed(() -> onGameEnd.onGameEnd(false), 1000);
                    }

                }, 1000);
            }

            flippedCards.clear();
        }
    }
}
