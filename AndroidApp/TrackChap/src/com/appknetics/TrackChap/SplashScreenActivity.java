package com.appknetics.TrackChap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.appknetics.TrackChap.R;
import com.fragments.Constantclass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreenActivity extends Activity {

	private TextView title;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);
		
		preferences = getSharedPreferences("tracker",
				MODE_PRIVATE);
		editor = preferences.edit();
		editor.putString("inside_app","inside");
		editor.commit();
		
		// if user login then call configration website other wise not called
		int flag=preferences.getInt("configration_flag",0);
		if(flag==1){			
			new Configuration().execute();		
		}
		
//		title=(TextView)findViewById(R.id.title);
//		Spannable wordtoSpan = new SpannableString("Appknetics");          
//		wordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#00c3f0")), 3,10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);    
//		title.setText(wordtoSpan);
		Handler handler=new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				String notcome=preferences.getString("notcome","");
				if(notcome.equals("notcome")){
					Intent intent=new Intent(SplashScreenActivity.this,HomeActivity.class);
					startActivity(intent);
					finish();	
				}else{
				
				Intent intent=new Intent(SplashScreenActivity.this,Tutorial.class);
				startActivity(intent);
				finish();				
				}
				}
		}, 2000);
		
		
		
	}
	class Configuration extends AsyncTask<String,Integer,Void>{

		ProgressDialog progressDialog;
		private StringBuilder stringBuilder;
		private String res,status;
		private String id_reg;
		private String name;
		private String user_latitude;
		private String user_longitude;
		private String end_latitude;
		private String end_longitude;
		private String latitude_zero;
		private String longitude_one;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			progressDialog = DialogFactory
//					.showOnlySpinningWheel(SplashScreenActivity.this);
		}
		
		@Override
		protected Void doInBackground(String... params) {

			HttpClient httpclient = new DefaultHttpClient();
			String	uid = preferences.getString("id", "");
			HttpGet httpGet = new HttpGet(ConstantUrlClass.Configuration+"?uid="+uid);
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
					res=stringBuilder.toString();
					Log.d("==res_confirm","=="+res);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			try {
				JSONObject obj=new JSONObject(res);
				status=obj.getString("status");
				Log.d("==status","=="+status);
				if(status.equals("true")){
					JSONObject data=obj.getJSONObject("data");
					JSONObject zero=data.getJSONObject("0");
					latitude_zero=zero.getString("value");
					
					JSONObject one=data.getJSONObject("1");
					longitude_one=one.getString("value"); 
					
					user_latitude=data.getString("user_latitude"); 
					user_longitude=data.getString("user_longitude");
					
					
					editor.putString("user_latitude",user_latitude);
					editor.putString("user_longitude",user_longitude);
					editor.putString("end_latitude",latitude_zero);
					editor.putString("end_longitude",longitude_one);
					editor.commit();	
					//progressDialog.dismiss();
//					Intent intent=new Intent(SplashScreenActivity.this,HomeActivity.class);
//					startActivity(intent);
//					finish();
					//Toast.makeText(SplashScreenActivity.this, "Login Sucessfull",Toast.LENGTH_SHORT).show();
				}else{
					//Toast.makeText(SplashScreenActivity.this, "Invalid mobile number",Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
			}
			
		}
	}
}
