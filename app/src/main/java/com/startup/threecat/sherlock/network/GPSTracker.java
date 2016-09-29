package com.startup.threecat.sherlock.network;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.startup.threecat.sherlock.model.ConstantData;


/**
 * Created by Dell on 4/19/2016.
 */
public class GPSTracker implements LocationListener {

    private Activity mContext;
    private LocationManager locationManager;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 60;// 5 kilometer
    private static final long MIN_TIME_BW_UPDATES = 60 * 60; // 60 minute
    private static Location mLocation = null;
    private static double latitude;
    private static double longitude;


    public GPSTracker(Activity context) {
        this.mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
    }

    public boolean checkPermissionAndUpdateLocation() {

        boolean permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permission) {
            requestUpdateLocation();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // do something to rationale permission
            } else {
                ActivityCompat.requestPermissions(mContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        ConstantData.MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
        return permission;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e("tuton", "change");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public boolean isGetLocation() {
        boolean isGetLocation = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGetLocation;
    }

    public void requestUpdateLocation() {

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean permission = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (permission) {

            if (isGPSEnabled) {
                mLocation = locationManager.getLastKnownLocation(
                        LocationManager.GPS_PROVIDER);
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        GPSTracker.this);

                if (mLocation != null) {
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                    Log.e("tuton", "mLocation khac null");
                } else {
                    Log.e("tuton", "mLocation null");
                }
                Log.e("tuton", "gps enable");
            }
            else if (isNetworkEnabled) {
                mLocation = locationManager.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER);
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        GPSTracker.this
                );

                if (mLocation != null) {
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                }
                Log.e("tuton", "network enable");
            }
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void removeLocationUpdate() {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }


}
