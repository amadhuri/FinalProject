package com.example.madhuri.myapplication.backend;
import com.example.Jokes;

/** The object model for the data we are sending through endpoints */
public class MyBean {

    private String myData;

    public String getJoke() {

        return myData;
    }

    public void setJoke() {
        Jokes jokes = new Jokes();
        myData = jokes.getJoke();
    }
}