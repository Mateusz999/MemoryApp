package com.example.memoryapp.Classes;

import android.widget.ImageView;

import java.io.Serializable;

public class CardState implements Serializable {

    public int imageResId;
    public boolean isFlipped;
    public boolean isMatched;
}
