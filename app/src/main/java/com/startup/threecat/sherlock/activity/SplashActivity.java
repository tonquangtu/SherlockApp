package com.startup.threecat.sherlock.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.databasehelper.DatabaseUtil;
import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.Person;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar pgbLoader;
    private ArrayList<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pgbLoader = (ProgressBar)findViewById(R.id.pgbLoader);
        pgbLoader.setIndeterminate(true);
        new TaskLoadInfoFromDatabase().execute();
    }

    private class TaskLoadInfoFromDatabase extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            persons = DatabaseUtil.getAllPerson(SplashActivity.this);
            SystemClock.sleep(300);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pgbLoader.setVisibility(View.GONE);
            Bundle data = new Bundle();
            data.putSerializable(ConstantData.PERSONS_TAG, persons);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra(ConstantData.PACKAGE, data);
            SplashActivity.this.startActivity(intent);
            finish();
        }
    }
}
