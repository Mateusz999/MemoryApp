package com.example.memoryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class Navigator {
    private final Context context;

    public Navigator( Context context){
        this.context = context;
    }

    public void goToMenu(){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

}
