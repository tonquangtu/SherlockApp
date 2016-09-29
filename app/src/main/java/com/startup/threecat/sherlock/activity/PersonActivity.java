package com.startup.threecat.sherlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.adapter.InfoPersonPagerAdapter;
import com.startup.threecat.sherlock.adapter.RVMovementsAdapter;
import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.Movement;
import com.startup.threecat.sherlock.model.Person;

import java.util.ArrayList;


public class PersonActivity extends AppCompatActivity {

    private Person person;
    private int position;
    private ViewPager vpInfo;
    private TabLayout tblInfo;
    private Toolbar toolbar;
    private InfoPersonPagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra(ConstantData.PACKAGE);
        person = (Person)data.getSerializable(ConstantData.PERSON_TAG);
        position = data.getInt(ConstantData.POSITION);

        vpInfo = (ViewPager)findViewById(R.id.vpInfo);
        tblInfo = (TabLayout)findViewById(R.id.tabLayoutInfoPerson);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        pagerAdapter = new InfoPersonPagerAdapter(getSupportFragmentManager(), person);

        vpInfo.setAdapter(pagerAdapter);
        tblInfo.setupWithViewPager(vpInfo);
        initToolbar();

    }

    public void initToolbar() {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("  About " + person.getName());
        actionBar.setIcon(R.drawable.search_people);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_info_person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home :
                handleSendResultComebackActivity();
                finish();
                break;
            case R.id.mn_add_movement :
                handleAddMovement();
                break;
        }
        return true;
    }

    public void handleAddMovement() {

        String idPerson = person.getId();
        Intent intent = new Intent(this, AddMovementActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConstantData.ID_PERSON_TAG, idPerson);
        intent.putExtra(ConstantData.PACKAGE, bundle);
        startActivityForResult(intent, ConstantData.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ConstantData.REQUEST_CODE && resultCode == ConstantData.RESULT_CODE_HAVE_RESULT) {
            Bundle bundle = data.getBundleExtra(ConstantData.PACKAGE);
            ArrayList<Movement> listMovementAdd = (ArrayList<Movement>) bundle.getSerializable(ConstantData.LIST_MOVEMENT);
            int oldSize = person.getMovements().size();
            person.getMovements().addAll(listMovementAdd);
            int newSize = person.getMovements().size();
            RVMovementsAdapter rvMovementsAdapter = pagerAdapter.getMovementFragment().getRvMovementsAdapter();
            for(int i = oldSize; i < newSize; i++) {
                rvMovementsAdapter.notifyItemInserted(i);
            }
            pagerAdapter.getMovementFragment().changeVisibleView(View.VISIBLE, View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        handleSendResultComebackActivity();
        super.onBackPressed();
    }

    public void handleSendResultComebackActivity() {

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantData.PERSON_TAG, person);
        bundle.putInt(ConstantData.POSITION, position);
        intent.putExtra(ConstantData.PACKAGE, bundle);
        setResult(ConstantData.RESULT_CODE_INFO_PERSON, intent);
    }

}
