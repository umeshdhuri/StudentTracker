package com.fragments;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.appknetics.TrackChap.DialogFactory;
import com.appknetics.TrackChap.Login_Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public abstract class HttpGetClass extends AsyncTask<Void,Void,Void> {
	
	private StringBuilder stringBuilder;
	private String res,exception="";
	String url;
	private ProgressDialog progressDialog;
	Activity activity;
	public HttpGetClass(String url,Activity activity) {
			// TODO Auto-generated constructor stub	    	
		this.url=url;
		this.activity=activity;
		}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = DialogFactory
				.showOnlySpinningWheel(activity);
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub	
		Httpget(url);
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		onCancelChosen(res,exception,progressDialog);
	}
	
	 public String Httpget(String abc){			
			HttpClient httpclient = new DefaultHttpClient();
			// phone_str=phone1.getText().toString().trim();
			HttpGet httpGet = new HttpGet(abc);			
			try {
				HttpResponse response = httpclient.execute(httpGet);
				InputStream inputStream = response.getEntity().getContent();

				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream);

				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);

				stringBuilder = new StringBuilder();

				String bufferedStrChunk = null;

				while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
					stringBuilder.append(bufferedStrChunk);
				}
				res = stringBuilder.toString();
				Log.d("==TrackStudent", "==" + res);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				exception=e.getMessage();
				e.printStackTrace();
			}
		return res;
	 }
	 public abstract void onCancelChosen(String status,String exception,ProgressDialog progressDialog);
}
