package com.example.memoryapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;

public class MemoryCard {
    private final View cardView;
    public final ImageView icon;
    private boolean isFlipped = false;


    /*
    * W konstruktorze przekazujemy widok naszej karty, przypisujemy do zmiennych klasy podstawowe informacje,
    * dodatkowo w konstrukturze przekazujemi interfejs onCardFlip który b
    * */
    public MemoryCard(View cardView, OnCardFlipListener cardListener) {
        this.cardView = cardView;
        this.icon = cardView.findViewById(R.id.cardIcon);
        icon.setVisibility(View.GONE);
        cardView.setOnClickListener(v -> cardListener.onCardFlip(this));
    }
    /*
    *   Metoda flip implementuje dwie animacje, które są kolejno pierwszą oraz drugą fazą obrotu
    *
    * */
    public void flip() {
        ObjectAnimator flipOut = ObjectAnimator.ofFloat(cardView, "rotationY", 0f, 90f);
        ObjectAnimator flipIn = ObjectAnimator.ofFloat(cardView, "rotationY", -90f, 0f);


        // ustawiamy nasłuchiwacza dla animacji flipOut,
        // gdy flipout sie zakończy, ustawiamy odpowiednią widoczność naszej emotki i następnie kończymy drugą fazą obrotu
        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                icon.setVisibility(isFlipped ? View.GONE : View.VISIBLE);
                flipIn.start();
                isFlipped = !isFlipped;
            }
        });
        flipOut.start();
    }

    public void forcedFlip(){
        if(!isFlipped) flip();
    }

    public void hideCard(){
        cardView.setVisibility(View.INVISIBLE);
    }

    public void setIcon(int drawableResId) {
        icon.setImageResource(drawableResId);
    }

}
