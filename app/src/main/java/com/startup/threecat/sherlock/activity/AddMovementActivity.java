package com.startup.threecat.sherlock.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.databasehelper.DatabaseUtil;
import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.InfoLocation;
import com.startup.threecat.sherlock.model.ListInfoLocation;
import com.startup.threecat.sherlock.model.Movement;
import com.startup.threecat.sherlock.network.ConfigurationNetwork;
import com.startup.threecat.sherlock.network.GPSTracker;
import com.startup.threecat.sherlock.network.ServiceAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMovementActivity extends AppCompatActivity {

    private ArrayList<Movement> listAddMovement;
    private String idPerson;
    private ImageButton imgBtnLocation;
    private EditText edtMovementLocation;
    private EditText edtMovementNote;
    private Button btnSave;
    private Button btnReturn;
    GPSTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movement);

        Intent intent = getIntent();
        idPerson = intent.getBundleExtra(ConstantData.PACKAGE).getString(ConstantData.ID_PERSON_TAG);
        listAddMovement = new ArrayList<>();
        gpsTracker = new GPSTracker(this);
        initView();
    }

    public void initView() {

        imgBtnLocation = (ImageButton)findViewById(R.id.imgBtnLocation);
        edtMovementLocation = (EditText)findViewById(R.id.edtMovementLocation);
        edtMovementNote = (EditText)findViewById(R.id.edtMovementNote);
        btnReturn = (Button)findViewById(R.id.btnReturn);
        btnSave = (Button)findViewById(R.id.btnSave);

        imgBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChooseLocation();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveMovement();
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendResultComeBack();
                AddMovementActivity.this.finish();
            }
        });
    }

    public void handleChooseLocation() {

        boolean isGetLocation = gpsTracker.isGetLocation();
        if(!isGetLocation) {
            confirmEnableGPSDialog();

        }else {
            boolean isPermission = gpsTracker.checkPermissionAndUpdateLocation();
            if(isPermission) {
                loadLocationSuggestion();
            }
        }
    }

    public void handleSaveMovement() {

        String movementNote = edtMovementNote.getText().toString();
        String movementLocation = edtMovementLocation.getText().toString();
        if(movementLocation.trim().equals("")) {
            edtMovementLocation.setError("Required value !");
            edtMovementLocation.requestFocus();
        }else if(movementNote.trim().equals("")) {
            edtMovementNote.setError("Required value !");
            edtMovementNote.requestFocus();
        }else {
            String time = new SimpleDateFormat("yyy-MM-dd HH-mm-ss").format(new Date());
            String id = System.currentTimeMillis() + "_" + "movement";
            Movement movement = new Movement(id, movementLocation, movementNote, time);
            boolean isAddSuccess = DatabaseUtil.addMovementForPerson(this, idPerson, movement);
            if(isAddSuccess) {
                listAddMovement.add(movement);
                Toast.makeText(this, "Add movement successful", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Can't save movement", Toast.LENGTH_LONG).show();
            }
        }
    }

    public ProgressDialog showProgressDialog() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading location ...");
        progressDialog.show();
        return progressDialog;
    }

    public void confirmEnableGPSDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_turn_gps);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.turn_gps, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                AddMovementActivity.this.startActivity(intent);
            }
        });
        builder.create().show();
    }

    public Map<String, String> getTagToServer(double lat, double longtitude) {

        String sLocation = lat + "," + longtitude;
        Map<String, String> tag = new HashMap<>();
        tag.put(ConstantData.LOCATION_TAG, sLocation);
        tag.put(ConstantData.RADIUS_TAG, ConstantData.RADIUS +"");
        tag.put(ConstantData.NAME_TAG, ConstantData.VALUE_NAME);
        tag.put(ConstantData.TYPES_TAG , ConstantData.TYPE_LOCATION);
        tag.put(ConstantData.KEY_TAG, getResources().getString(R.string.API_KEY2));
        String key = getResources().getString(R.string.API_KEY2);
        String link = ConstantData.BASE_LINK_GOOGLE_SERVICE_PLACE + ConstantData.LINK_NEAR_PLACE + "?" + "location=" + lat + "," + longtitude + "&radius=5000&types=food&name=cruise&key=" + key;
        Log.e("tuton", link);
        Log.e("tuton", "lat : " + lat);
        Log.e("tuton", "long : " + longtitude);

        return tag;
    }

    public void showSuggestionLocation(ArrayList<InfoLocation> infoLocations) {

        Intent intent = new Intent(this, SuggestionLocationActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(ConstantData.INFO_LOCATIONS, infoLocations);
        intent.putExtra(ConstantData.PACKAGE, data);
        startActivityForResult(intent, ConstantData.REQUEST_CODE);
    }
    
    @Override
    public void onBackPressed() {
        handleSendResultComeBack();
        super.onBackPressed();
    }

    public void handleSendResultComeBack() {
        Intent intent = getIntent();
        if(listAddMovement.size() == 0) {
            setResult(ConstantData.RESULT_CODE_NO_RESULT, intent);
        }else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantData.LIST_MOVEMENT, listAddMovement);
            intent.putExtra(ConstantData.PACKAGE, bundle);
            setResult(ConstantData.RESULT_CODE_HAVE_RESULT, intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ConstantData.REQUEST_CODE && resultCode == ConstantData.RESULT_CODE_HAVE_RESULT) {
            Bundle bundle = data.getBundleExtra(ConstantData.PACKAGE);
            InfoLocation infoLocation = (InfoLocation)bundle.getSerializable(ConstantData.INFO_LOCATION);
            edtMovementLocation.setText(infoLocation.getVicinity());
            edtMovementNote.setText(infoLocation.getName());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ConstantData.MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION :
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    gpsTracker.requestUpdateLocation();
                    loadLocationSuggestion();
                }else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
        }
    }

    public void loadLocationSuggestion() {

        final ProgressDialog progressDialog = showProgressDialog();
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        Map<String, String> tag = getTagToServer(latitude, longitude);
        ServiceAPI serviceAPI = ConfigurationNetwork.getServiceAPI();
        Call<ListInfoLocation> call = serviceAPI.loadListInfoLocation(tag);
        call.enqueue(new Callback<ListInfoLocation>() {

                @Override
                public void onResponse(Call<ListInfoLocation> call, Response<ListInfoLocation> response) {
                    progressDialog.cancel();
                    if(response.isSuccessful()) {
                        ListInfoLocation listInfoLocation = response.body();
                        ArrayList<InfoLocation> results = listInfoLocation.getResults();
                        showSuggestionLocation(results);
                    }else {
                        Toast.makeText(AddMovementActivity.this, "Can't load suggestion location",
                                Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ListInfoLocation> call, Throwable t) {
                    progressDialog.cancel();
                    Toast.makeText(AddMovementActivity.this, "Can't load suggestion location",
                            Toast.LENGTH_LONG).show();
                }
        });
    }

    @Override
    protected void onStop() {
        // remove update location
        gpsTracker.removeLocationUpdate();
        super.onStop();
    }
}
