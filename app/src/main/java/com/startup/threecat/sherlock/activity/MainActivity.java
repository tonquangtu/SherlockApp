package com.startup.threecat.sherlock.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.adapter.RVListPersonAdapter;
import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.Person;
import com.startup.threecat.sherlock.util.SpaceItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Person> persons;
    private RecyclerView rvListPerson;
    private ImageView imgNoData;
    private Toolbar toolbar;
    private StaggeredGridLayoutManager layoutManager;
    private static final int NUM_COL = 2;
    private static final int SPACE_ITEM = 20;
    private static final String TITLE = "Sherlock";
    public static final int ADD_PERSON = 1;
    public static final int PERSON_INFO = 2;
    private RVListPersonAdapter rvListPersonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConstantData.PACKAGE);
        persons = (ArrayList<Person>)bundle.getSerializable(ConstantData.PERSONS_TAG);
        initView();
        initToolbar();
        if(persons == null || persons.size() == 0) {
            imgNoData.setVisibility(View.VISIBLE);
            rvListPerson.setVisibility(View.GONE);
            persons = new ArrayList<>();
        }else {
            imgNoData.setVisibility(View.GONE);
            rvListPerson.setVisibility(View.VISIBLE);
        }
        initRVView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.mn_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearchPerson(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idItem = item.getItemId();
        switch (idItem) {
            case R.id.mn_add_person :
                handleAddPerson();
                break;
            case R.id.mn_upload :
                handleUpload();
                break;
        }
       return true;
    }

    public void initView() {
        rvListPerson = (RecyclerView)findViewById(R.id.rvListPerson);
        imgNoData = (ImageView)findViewById(R.id.imgNoData);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
    }

    public void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle(TITLE);
        actionBar.setIcon(getResources().getDrawable(R.drawable.search_people));
    }

    public void initRVView() {

        layoutManager = new StaggeredGridLayoutManager(NUM_COL,StaggeredGridLayoutManager.VERTICAL);
        SpaceItem spaceItem = new SpaceItem(SPACE_ITEM, SpaceItem.GRID);
        rvListPerson.setLayoutManager(layoutManager);
        rvListPerson.addItemDecoration(spaceItem);
        initAdapter();
        rvListPerson.setAdapter(rvListPersonAdapter);
    }

    public void handleItemClick(int position ) {

        Person person = persons.get(position);
        Intent intent = new Intent(this, PersonActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(ConstantData.PERSON_TAG, person);
        data.putInt(ConstantData.POSITION, position);
        intent.putExtra(ConstantData.PACKAGE, data);
        this.startActivityForResult(intent,PERSON_INFO);
    }

    public void handleItemLongClick(final int position) {

        String title = "Are you want to delete \n information of " + persons.get(position).getName();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removePerson(position);
            }
        });
        builder.create().show();
    }

    public void handleSearchPerson(String query) {
        query = query.trim();
        if(!query.equals("")) {
            Intent intent = new Intent(this, ResultSearchActivity.class);
            Bundle data = new Bundle();
            data.putSerializable(ConstantData.PERSONS_TAG, persons);
            data.putString(ConstantData.QUERY, query);
            intent.putExtra(ConstantData.PACKAGE, data);
            startActivity(intent);
        }
    }

    public void handleAddPerson() {

        Intent intent = new Intent(this, AddPersonActivity.class);
        startActivityForResult(intent, ADD_PERSON);
    }

    public void handleUpload() {}

    public boolean removePerson(int position) {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_PERSON && resultCode == ConstantData.RESULT_CODE_ADD_PERSON) {

            ArrayList<Person> result ;
            Bundle bundle = data.getBundleExtra(ConstantData.PACKAGE);
            result = (ArrayList<Person>) bundle.getSerializable(ConstantData.RESULT_ADD_PERSON);
            if(result != null && result.size() != 0) {
                int oldSize = persons.size();
                persons.addAll(result);
                int newSize = persons.size();
                if(oldSize == 0 && imgNoData.getVisibility() == View.VISIBLE) {
                    imgNoData.setVisibility(View.GONE);
                }
                if(rvListPerson.getVisibility() == View.GONE) {
                    rvListPerson.setVisibility(View.VISIBLE);
                }
                for(int i = oldSize; i < newSize; i++) {
                    rvListPersonAdapter.notifyItemInserted(i);
                }
            }
        }
    }

    public void initAdapter() {
        rvListPersonAdapter = new RVListPersonAdapter(this, persons);
        rvListPersonAdapter.setListener(new RVListPersonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                handleItemClick(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                handleItemLongClick(position);
            }
        });
    }
}
