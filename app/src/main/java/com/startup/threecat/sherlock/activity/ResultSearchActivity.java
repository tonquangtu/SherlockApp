package com.startup.threecat.sherlock.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.adapter.RVListPersonAdapter;
import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.Person;
import com.startup.threecat.sherlock.util.StringUtil;

import java.util.ArrayList;

public class ResultSearchActivity extends AppCompatActivity {

    private RecyclerView rvListPerson;
    private RVListPersonAdapter rvListPersonAdapter;
    private ProgressBar pgbLoader;
    private Toolbar toolbar;
    private ImageView imgNoData;
    private String query;
    private ArrayList<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra(ConstantData.PACKAGE);
        query = data.getString(ConstantData.QUERY);
        persons = (ArrayList<Person>) data.getSerializable(ConstantData.PERSONS_TAG);
        initView();
        new TaskSearch().execute();

    }

    public void initView() {

        rvListPerson = (RecyclerView)findViewById(R.id.rvListPerson);
        pgbLoader = (ProgressBar)findViewById(R.id.pgbLoader);
        imgNoData = (ImageView)findViewById(R.id.imgNoData);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        pgbLoader.setVisibility(View.VISIBLE);
        rvListPerson.setVisibility(View.GONE);
        imgNoData.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(ConstantData.TITLE_RESULT + "'" + query.trim() + "'");
        actionBar.setIcon(getResources().getDrawable(R.drawable.search_people));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    private class TaskSearch extends AsyncTask<Void, Void, ArrayList<Person>> {

        @Override
        protected ArrayList<Person> doInBackground(Void... params) {
            return search(query);
        }

        @Override
        protected void onPostExecute(ArrayList<Person> persons) {
            super.onPostExecute(persons);
            ArrayList<Person> result = persons;
            if(result.size() == 0) {
                pgbLoader.setVisibility(View.GONE);
                imgNoData.setVisibility(View.VISIBLE);
            }else {
                initRV(result);
                pgbLoader.setVisibility(View.GONE);
            }
        }
    }

    public ArrayList<Person> search(String query) {

        query = query.toLowerCase();
        query = StringUtil.removeAccent(query);
        ArrayList<String> subQuerys = split(query);
        ArrayList<Person> result = new ArrayList<>();
        for (Person person : persons) {

            String name = person.getName().toLowerCase().trim();
            name = StringUtil.removeAccent(name);
            int n = subQuerys.size();
            for (int i = 0; i < n; i++) {
                if(name.contains(subQuerys.get(i))) {
                    result.add(person);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * not check condition input, check trim, query are standration
     * @param query
     * @return
     */
    public ArrayList<String> split(String query) {

        ArrayList<String> subs = new ArrayList<>();
        boolean flag = false;
        char [] arrPattern = {' ', '-', ':', '_', '@', '!'};
        int lenght = query.length();
        int lenghtPettrn = arrPattern.length;
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < lenght; i++) {
            char temp = query.charAt(i);
            for(int j = 0; j < lenghtPettrn; j++) {
                if(arrPattern[j] == temp) {
                    if(builder.length() != 0) {
                        subs.add(builder.toString());
                    }
                    flag = true;
                    builder = new StringBuilder();
                    break;
                }else {
                    flag = false;
                }
            }
            if(flag == false) {
                builder.append(temp);
            }
        }
        if(builder.length() != 0) {
            subs.add(builder.toString());

        }
        return subs;
    }

    public void initRV(ArrayList<Person> result) {

        rvListPerson.setVisibility(View.VISIBLE);
        rvListPersonAdapter = new RVListPersonAdapter(this, result);
        rvListPerson.setAdapter(rvListPersonAdapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvListPerson.setLayoutManager(layoutManager);
        rvListPerson.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
