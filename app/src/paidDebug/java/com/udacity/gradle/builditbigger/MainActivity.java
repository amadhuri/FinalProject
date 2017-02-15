package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.madhuri.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.capstone.androidlibrary.JokeActivity;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    static final String EXTRA_JOKE_STRING = "joke";
    String testJokeString =  null;
    private ProgressBar mProgressBar;
    private JokesAsyncTask mJokesAsyncTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        createJokesAsyncTask(mProgressBar);
        executeJokesAsyncTask();
    }

    public JokesAsyncTask createJokesAsyncTask(ProgressBar progressBar) {
        mJokesAsyncTask = new JokesAsyncTask(getApplicationContext(), progressBar);
        return mJokesAsyncTask;
    }
    public void executeJokesAsyncTask() {
        if (mJokesAsyncTask != null )
            mJokesAsyncTask.execute();
    }



    class JokesAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
        private  MyApi myApiService = null;
        private Context mContext;
        private ProgressBar mActivityProgressBar = null;

        public JokesAsyncTask(Context context, ProgressBar progressBar) {
            this.mContext = context;
            this.mActivityProgressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mActivityProgressBar !=null)
                mActivityProgressBar.setVisibility(View.VISIBLE);

        }
        @Override
        protected String doInBackground(Pair<Context, String>... params) {
            if(myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }
            try {
                return myApiService.bringJoke().execute().getJoke();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(getApplicationContext(),JokeActivity.class);
            testJokeString = result;
            intent.putExtra(EXTRA_JOKE_STRING,result);
            startActivity(intent);
            if (mActivityProgressBar != null)
                mActivityProgressBar.setVisibility(View.GONE);

        }
    }

}
