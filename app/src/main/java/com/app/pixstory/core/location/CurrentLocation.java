package com.app.pixstory.core.location;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

public class CurrentLocation {

    public static CurrentLocation currentLocation;
    private CurrentLocationListener currentLocationListener;
    private Context context;
    AppLocationService appLocationService;

    private CurrentLocation(){

    }

    public static CurrentLocation getInstance(){
        if (currentLocation == null){
            currentLocation = new CurrentLocation();
        }
        return currentLocation;
    }

    public interface CurrentLocationListener{
        void onSuccess(String countryName, String countryCode);
    }

    public void setUp(Context context, CurrentLocationListener currentLocationListener){
        this.context = context;
        this.currentLocationListener = currentLocationListener;
        setUpLocation();

    }

    public void setUpLocation(){
        appLocationService = new AppLocationService(context);
        gpsLocation();
        showAddress();
    }

    private void gpsLocation(){
        Location gpsLocation = appLocationService
                .getLocation(LocationManager.NETWORK_PROVIDER);
        if (gpsLocation != null) {
            double latitude = gpsLocation.getLatitude();
            double longitude = gpsLocation.getLongitude();
            String result = "Latitude: " + gpsLocation.getLatitude() +
                    " Longitude: " + gpsLocation.getLongitude();
            //  tvAddress.setText(result);

        } else {
            showSettingsAlert();
        }
    }

    private void showAddress(){
        Location location = appLocationService
                .getLocation(LocationManager.NETWORK_PROVIDER);

        //you can hard-code the lat & long if you have issues with getting it
        //remove the below if-condition and use the following couple of lines
        //double latitude = 37.422005;
        //double longitude = -122.084095

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            LocationAddress.getAddressFromLocation(latitude, longitude,
                    context.getApplicationContext(), new  CurrentLocation.GeocoderHandler());
        } else {
            showSettingsAlert();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String countryCode;
            String countryName = "INDIA";
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    countryCode = bundle.getString("countryCode");
                    countryName = bundle.getString("countryName");
                    break;
                default:
                    countryCode = null;
            }

            currentLocationListener.onSuccess(countryName, countryCode);
          //  binding.ccp.setDefaultCountryUsingNameCodeAndApply(countryCode);
         //   binding.ccp.getDefaultCountryCode();
          //  binding.homeCountry.setText(countryName);
        }
    }

}
