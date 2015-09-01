package com.fragments;

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
    private int i;
    public LocationAddress(int i) {
		// TODO Auto-generated constructor stub
    	this.i=i;
	}

	public  void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
            	if(context==null){
            		
            	}else{
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        if(i==1){
                        	for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i)).append("\n");
                             }
                        	sb.append(address.getLocality()).append("\n");
                        	sb.append(address.getPostalCode()).append("\n");
                        	sb.append(address.getCountryName());
                        }
                        
//                       sb.append(address.getLocality()).append("\n");
//                        sb.append(address.getSubAdminArea()).append("\n");
//                        sb.append(address.getAdminArea()).append("\n");
                       // sb.append(address.getPostalCode()).append("\n");
                      //  sb.append(address.getCountryName());
                        if(i==2){
                        sb.append(address.getThoroughfare());
                        }
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
//                        result = "Latitude: " + latitude + " Longitude: " + longitude +
//                                "\n\nAddress:\n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
//                        result = "Latitude: " + latitude + " Longitude: " + longitude +
//                                "\n Unable to get address for this lat-long.";
                        result="No updated data found.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
            }
        };
        thread.start();
    }
}