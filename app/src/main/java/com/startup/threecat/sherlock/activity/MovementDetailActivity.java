package com.startup.threecat.sherlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.Movement;

public class MovementDetailActivity extends AppCompatActivity {

    TextView txtName;
    TextView txtTime;
    TextView txtNote;
    TextView txtLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_detail);

        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra(ConstantData.PACKAGE);
        Movement movement = (Movement)data.getSerializable(ConstantData.MOVEMENT);
        String name = data.getString(ConstantData.NAME_PERSON);

        txtName = (TextView)findViewById(R.id.txtMPersonName);
        txtNote = (TextView)findViewById(R.id.txtMMovementNote);
        txtLocation = (TextView)findViewById(R.id.txtMMovementLocation);
        txtTime = (TextView)findViewById(R.id.txtMTime);

        txtName.setText(name);
        txtLocation.setText(movement.getLocation());
        txtNote.setText(movement.getMovementNote());
        txtTime.setText(movement.getTime());

    }
}
