package com.example.memoryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ChanceFactory {

    private final LayoutInflater inflater;
    private LinearLayout linearLayout;
    private final Context context   ;

    private int amountOfChances = 5;

    private int icon = R.drawable.chance;
    public ChanceFactory(int life, LayoutInflater inflater, LinearLayout linearLayout, Context context){
        this.inflater = inflater;
        this.linearLayout = linearLayout;
        this.context = context;
        this.amountOfChances = life;
    }

    public List<ImageView> printChances() {
        List<ImageView> chances = new ArrayList<>();

        for (int i = 0; i < amountOfChances; i++) {
            ImageView singleChance = new ImageView(context);
            singleChance.setImageResource(icon);
            int size = (int) (32 * context.getResources().getDisplayMetrics().density); // 32dp
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(8, 8, 8, 8);
            singleChance.setLayoutParams(params);

            linearLayout.addView(singleChance);
            chances.add(singleChance);
        }

        return chances;
    }

    public void setAmountOfChances(int value){
        this.amountOfChances = value;
    }
}
