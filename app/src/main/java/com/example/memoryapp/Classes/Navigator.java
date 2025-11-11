package com.example.memoryapp.Classes;

import android.content.Context;
import android.content.Intent;

import com.example.memoryapp.MainActivity;

public class Navigator {
    private final Context context;

    public Navigator( Context context){
        this.context = context;
    }

    public void goToMenu(){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
