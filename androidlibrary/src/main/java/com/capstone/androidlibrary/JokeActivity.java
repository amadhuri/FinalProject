package com.capstone.androidlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    static final String EXTRA_JOKE_STRING = "joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        TextView textView = (TextView) findViewById(R.id.jokeTextView);
        String jokeString = getIntent().getExtras().getString(EXTRA_JOKE_STRING);
        if(jokeString != null && !jokeString.isEmpty()) {
            textView.setText(jokeString);
        }
        else {
            textView.setText("Sorry ! No Joke");
        }
    }
}