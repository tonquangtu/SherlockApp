package com.startup.threecat.sherlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.adapter.RVListSuggestionLocationAdapter;
import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.InfoLocation;
import com.startup.threecat.sherlock.util.SpaceItem;

import java.util.ArrayList;

public class SuggestionLocationActivity extends AppCompatActivity {

    private ArrayList<InfoLocation> infoLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_location);

        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra(ConstantData.PACKAGE);
        infoLocations = (ArrayList<InfoLocation>)data.getSerializable(ConstantData.INFO_LOCATIONS);

        RecyclerView rvListSuggestion = (RecyclerView)findViewById(R.id.rvListSuggestionLocation);
        Button btnCancel = (Button)findViewById(R.id.btnCancel);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        SpaceItem spaceItem = new SpaceItem(5, SpaceItem.VERTICAL);
        rvListSuggestion.setLayoutManager(layoutManager);
        rvListSuggestion.addItemDecoration(spaceItem);
        RVListSuggestionLocationAdapter adapter = new RVListSuggestionLocationAdapter(this, infoLocations);
        adapter.setListener(new RVListSuggestionLocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                handleReturnResult(infoLocations.get(position));
            }
        });
        rvListSuggestion.setAdapter(adapter);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReturnResult(null);
            }
        });
    }

    public void handleReturnResult(InfoLocation infoLocation) {

        Intent intent = getIntent();

        if(infoLocation == null) {
            setResult(ConstantData.RESULT_CODE_NO_RESULT, intent);
        }else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantData.INFO_LOCATION, infoLocation);
            intent.putExtra(ConstantData.PACKAGE, bundle);
            setResult(ConstantData.RESULT_CODE_HAVE_RESULT, intent);
        }
        finish();
    }


}
