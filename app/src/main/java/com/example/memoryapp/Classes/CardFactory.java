package com.example.memoryapp.Classes;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;

import com.example.memoryapp.Interfaces.IGameStateSaver;
import com.example.memoryapp.Interfaces.OnCardFlipListener;
import com.example.memoryapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardFactory {

    private final LayoutInflater inflater;
    private final GridLayout gridLayout;
    private final GameBoard gameBoard;       // zamiast OnCardFlipListener
    private final IGameStateSaver saver;

    private static final int[] ICONS = {
            R.drawable.frightened, R.drawable.sad, R.drawable.angry, R.drawable.loving,
            R.drawable.shy, R.drawable.happiness, R.drawable.border, R.drawable.worried
    };

    public CardFactory(LayoutInflater inflater, GridLayout gridLayout, GameBoard gameBoard, IGameStateSaver saver) {
        this.inflater = inflater;
        this.gridLayout = gridLayout;
        this.gameBoard = gameBoard;
        this.saver = saver;
    }

    public List<MemoryCard> createCards() {
        List<Integer> pairedIcons = generatePairedIcons();
        List<MemoryCard> cards = new ArrayList<>();

        for (int i = 0; i < pairedIcons.size(); i++) {
            View cardView = inflater.inflate(R.layout.memory_card, gridLayout, false);
            gridLayout.addView(cardView);

            // konstruktor MemoryCard: View, GameBoard, IGameStateSaver
            MemoryCard card = new MemoryCard(cardView, gameBoard, saver);
            card.setIcon(pairedIcons.get(i));
            cards.add(card);
        }

        return cards;
    }

    // Prywatna metoda generująca sparowane i przetasowane ikony
    private List<Integer> generatePairedIcons() {
        List<Integer> pairedIcons = new ArrayList<>();
        for (int icon : ICONS) {
            pairedIcons.add(icon);
            pairedIcons.add(icon);
        }
        Collections.shuffle(pairedIcons);
        return pairedIcons;
    }
}
