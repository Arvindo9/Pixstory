package com.app.pixstory.core.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationAddress {
    private static final String TAG = "LocationAddress";

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {

        Thread t = new Thread() {

            public void run() {

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null, stateName = null,distName = null, countryName = null, countryCode = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 5);

                    if (addressList != null && addressList.size() > 0) {
                        for (int j = 0; j < addressList.size(); j++) {
                            Address address = addressList.get(j);
                            if ((address.getSubAdminArea() != null) && (address.getAdminArea() != null)) {

                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                    sb.append(address.getAddressLine(i)).append("\n");
                                }
                                sb.append(address.getLocality()).append("\n");
                                sb.append(address.getPostalCode()).append("\n");
                                sb.append(address.getCountryName());
                                result = sb.toString();

                                if (address.getSubAdminArea() != null) {
                                    distName = address.getSubAdminArea().trim();
                                }
                                if (address.getAdminArea() != null) {
                                    stateName = address.getAdminArea().trim();
                                }

                                if (address.getCountryName() != null){
                                    countryName = address.getCountryName().trim();
                                }

                                if (address.getCountryCode() != null){
                                    countryCode = address.getCountryCode().trim();
                                }
                                break;
                            }
                        }

                    }
                } catch (IOException e) {
                    result = "connect";
                    Log.e(TAG, "Unable to connect to GeoCoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        /*result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n\nAddress:\n" + result;*/
                        bundle.putString("address", result);
                        bundle.putString("stateName", stateName);
                        bundle.putString("distName", distName);
                        bundle.putString("countryName", countryName);
                        bundle.putString("countryCode", countryCode);
                        message.setData(bundle);
                    } else if (result == null) {
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n Unable to get address for this lat-long.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    if (result.equals("connect")) {
                        message.what = 3;
                        Bundle bundle = new Bundle();
                        result = "Not connect to GeoCoder";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        t.start();


    }
}
