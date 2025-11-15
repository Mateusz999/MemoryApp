package com.example.memoryapp.Classes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;

import com.example.memoryapp.Interfaces.IGameStateSaver;
import com.example.memoryapp.Interfaces.OnCardFlipListener;
import com.example.memoryapp.R;

public class MemoryCard {
    private final View cardView;
    public final ImageView icon;
    private final IGameStateSaver saver;
    private final GameBoard gameBoard;   // zamiast rzutowania listenera
    private boolean isFlipped = false;

    public MemoryCard(View cardView, GameBoard gameBoard, IGameStateSaver saver) {
        this.cardView = cardView;
        this.gameBoard = gameBoard;
        this.saver = saver;
        this.icon = cardView.findViewById(R.id.cardIcon);
        icon.setVisibility(View.GONE);

        cardView.setOnClickListener(v -> {
            gameBoard.onCardFlip(this); // GameBoard implementuje OnCardFlipListener
            if (saver != null) {
                GameState freshState = gameBoard.buildCurrentGameState();
                saver.saveState("AUTOMATIC", freshState);
            }
        });
    }

    public int getImageResId() {
        Object tag = icon.getTag();
        return tag instanceof Integer ? (int) tag : 0;
    }

    // Odtwarzanie dopasowania z zapisu
    public void setMatched(boolean matched) {
        cardView.setVisibility(matched ? View.INVISIBLE : View.VISIBLE);
    }

    public void flip() {
        ObjectAnimator flipOut = ObjectAnimator.ofFloat(cardView, "rotationY", 0f, 90f);
        ObjectAnimator flipIn = ObjectAnimator.ofFloat(cardView, "rotationY", -90f, 0f);

        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                icon.setVisibility(isFlipped ? View.GONE : View.VISIBLE);
                flipIn.start();

                isFlipped = !isFlipped;

                if (saver != null) {
                    GameState freshState = gameBoard.buildCurrentGameState();
                    saver.saveState("AUTOMATIC", freshState);
                }
            }
        });

        flipOut.start();
    }


    // Odtwarzanie stanu z zapisu (bez animacji)
    public void setFlipped(boolean flipped) {
        this.isFlipped = flipped;
        icon.setVisibility(flipped ? View.VISIBLE : View.GONE);
    }

    public void forcedFlip() {
        if (!isFlipped) flip();
    }

    public void hideCard() {
        cardView.setVisibility(View.INVISIBLE);
    }

    public void setIcon(int drawableResId) {
        icon.setImageResource(drawableResId);
        icon.setTag(drawableResId);
    }

    public boolean getFlipedState() {
        return isFlipped;
    }

    public boolean isMatched() {
        return cardView.getVisibility() == View.INVISIBLE;
    }
}
