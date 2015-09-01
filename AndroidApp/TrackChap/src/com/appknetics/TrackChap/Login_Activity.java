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
import com.fragments.EditProfile;
import com.fragments.HttpGetClass;
import com.google.android.gcm.GCMRegistrar;




import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class Login_Activity extends Activity implements OnClickListener {
	static String regId_gcm;
	Button confirm;
	EditText phone1;
	TextView contact_us;
	String phone_str,message,id;
	// shared prefreances declration
	SharedPreferences preferences,preferences1;
	SharedPreferences.Editor editor,editor1;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_act);
		//getActionBar().hide();
		preferences=getSharedPreferences("tracker",MODE_PRIVATE);
		editor=preferences.edit();
		
		preferences1=getSharedPreferences("tracker1",MODE_PRIVATE);
		editor1=preferences1.edit();
		
		Typeface confirm_font = Typeface.createFromAsset(getAssets(), "Gotham-Book.otf");
		Typeface contact_us_font = Typeface.createFromAsset(getAssets(), "Gotham-Light.otf");

		contact_us=(TextView)findViewById(R.id.contact_us);
		contact_us.setTypeface(contact_us_font);
		contact_us.setOnClickListener(this);
		confirm=(Button)findViewById(R.id.confirm);
		
		confirm.setTypeface(confirm_font);
		
		//Typeface phone1_font = Typeface.createFromAsset(getAssets(), "Gotham-Book.otf");
		
		phone1=(EditText)findViewById(R.id.phone_type);
		phone1.setTypeface(confirm_font);
		
		confirm.setOnClickListener(Login_Activity.this);
		
		GCMRegistrar.checkDevice(Login_Activity.this);
		GCMRegistrar.checkManifest(Login_Activity.this);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				"com.appknetics.TrackChap.DISPLAY_MESSAGE"));
		regId_gcm = GCMRegistrar.getRegistrationId(Login_Activity.this);
		
		if (regId_gcm.equals("")) {
			GCMRegistrar.register(this, "490725371053");
			Log.d("regi id","==--Registration id : == "+ GCMRegistrar.getRegistrationId(Login_Activity.this));
			
		} else {
			Log.d("info", "already registered as == " + regId_gcm);
		}
		
	}

	class Confirm extends AsyncTask<String,Integer,Void>{

		ProgressDialog progressDialog;
		private StringBuilder stringBuilder;
		private String res,status;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = DialogFactory
					.showOnlySpinningWheel(Login_Activity.this);
		}
		
		@Override
		protected Void doInBackground(String... params) {

			HttpClient httpclient = new DefaultHttpClient();
			phone_str=phone1.getText().toString().trim();
			HttpGet httpGet = new HttpGet(ConstantUrlClass.AUTHENTICATION_URL+"?phone="+phone_str);
			
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
					Log.d("==res","=="+res);
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
			progressDialog.dismiss();
			try {
				JSONObject obj=new JSONObject(res);
				status=obj.getString("status");
				Log.d("==status","=="+status);
				if(status.equals("true")){
					JSONObject data=obj.getJSONObject("data");
					message=data.getString("message");
					Log.d("==message","=="+message);
					dialogcalss(message);
					//Toast.makeText(getApplicationContext(), "valid",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(Login_Activity.this, "Invalid mobile number",Toast.LENGTH_SHORT).show();

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.confirm:
			if (new ConnectionDetector(this).isConnectingToInternet()) {
				
				if(phone1.length()>0)
				{
					new Confirm().execute();
				}
				else
				{
					Toast.makeText(Login_Activity.this, "Enter Mobile number",Toast.LENGTH_SHORT).show();
				}
				
			} else {
				Toast.makeText(Login_Activity.this, "No internet connectivity.",
						Toast.LENGTH_SHORT).show();
			}
			
			
			
//			new HttpGetClass(message,Login_Activity.this) {
//				
//				@Override
//				public void onCancelChosen(String status, String exception,ProgressDialog progressDialog) {
//					// TODO Auto-generated method stub
//					progressDialog.dismiss();
//				}
//			};
			break;
		case R.id.contact_us:
//			Animation slide = AnimationUtils.loadAnimation(Login_Activity.this, R.anim.up);
//			endtrip.startAnimation(slide);
			Intent i2 = new Intent(Login_Activity.this, Contactus_Admin.class);
			startActivity(i2);
			overridePendingTransition( R.anim.down, R.anim.up );
			
			break;
		default:
			break;
		}
	}
public void dialogcalss(String message2){

	final Dialog dialog=new Dialog(Login_Activity.this);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.setContentView(R.layout.dialog_layout);
	TextView title=(TextView)dialog.findViewById(R.id.title);
	title.setText(message2);
	Button yes=(Button)dialog.findViewById(R.id.yes);
	Button no=(Button)dialog.findViewById(R.id.no);
	
	yes.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
				regId_gcm = GCMRegistrar.getRegistrationId(Login_Activity.this);
			 if(regId_gcm.equals("")||(regId_gcm.isEmpty())){
					Toast.makeText(Login_Activity.this, "Some error occured, Please try again.",Toast.LENGTH_SHORT).show();
			 }else{
					new Confirm_authentication().execute();
			 }
			 
		
			dialog.dismiss();
		}
	});
	no.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	});
	dialog.show();
}
class Confirm_authentication extends AsyncTask<String,Integer,Void>{

	ProgressDialog progressDialog;
	private StringBuilder stringBuilder;
	private String res,status;
	private String id_reg;
	private String name;
	private String user_latitude;
	private String user_longitude;
	private String end_latitude;
	private String end_longitude;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = DialogFactory
				.showOnlySpinningWheel(Login_Activity.this);
	}
	
	@Override
	protected Void doInBackground(String... params) {

		HttpClient httpclient = new DefaultHttpClient();
		phone_str=phone1.getText().toString().trim();
		regId_gcm = GCMRegistrar.getRegistrationId(Login_Activity.this);		
		HttpGet httpGet = new HttpGet(ConstantUrlClass.Confirm_Aunthentication+"?phone="+phone_str+"&push_id="+regId_gcm+"&device_family="+"android");
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
		progressDialog.dismiss();
		try {
			JSONObject obj=new JSONObject(res);
			status=obj.getString("status");
			Log.d("==status","=="+status);
			if(status.equals("true")){
				JSONObject data=obj.getJSONObject("data");
				id_reg=data.getString("id");
				name=data.getString("name");
				String notcome_flag=preferences.getString("notcome_flag","");
				if(notcome_flag.equals("1")){
					editor.putString("notcome", "notcome");
					editor.commit();
				}
				
//				user_latitude=data.getString("user_latitude");
//				user_longitude=data.getString("user_longitude");
//				end_latitude=data.getString("end_latitude");
//				end_longitude=data.getString("end_longitude"); 
				Log.d("==message","=="+message);
				
				editor.putString("regid",regId_gcm);
				editor.putString("id",id_reg);
				editor.putString("name",name);
				editor.putString("number",phone1.getText().toString());			
				editor.putString("status","true");
//				editor.putString("user_latitude",user_latitude);
//				editor.putString("user_longitude",user_longitude);
//				editor.putString("end_latitude",end_latitude);
//				editor.putString("end_longitude",end_longitude);
				editor.commit();
				
				new Configuration().execute();
//				Intent intent=new Intent(Login_Activity.this,HomeActivity.class);
//				startActivity(intent);
//				finish();
			//	Toast.makeText(Login_Activity.this, "Login Sucessfull",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(Login_Activity.this, "Invalid mobile number",Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
//

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
	private String notcome_flag;

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = DialogFactory
				.showOnlySpinningWheel(Login_Activity.this);
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
				editor.putInt("configration_flag",1);
				editor.commit();	

				
				progressDialog.dismiss();
				
				 notcome_flag=preferences1.getString("firstLogin","1");

				 if(notcome_flag.equals("1")){	
						Intent i=new Intent(Login_Activity.this,EditProfile_FirstTime.class);
						startActivity(i);	
						finish();
						editor1.putString("firstLogin", "2");
						editor1.commit();
					}else{
					Intent i=new Intent(Login_Activity.this,HomeActivity.class);
					startActivity(i);
					finish();
					
					}
				 
				 
//				Intent intent=new Intent(Login_Activity.this,HomeActivity.class);
//				startActivity(intent);
//				finish();				
				Toast.makeText(Login_Activity.this, "Login Sucessfull",Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(Login_Activity.this, "Invalid mobile number",Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
		}
		
	}
}
private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
	@Override
	public void onReceive(Context context, Intent intent) {
		String newMessage = intent.getExtras().getString("message");
		// Waking up mobile if it is sleeping
		WakeLocker.acquire(getApplicationContext());

		/**
		 * Take appropriate action on this message depending upon your app
		 * requirement For now i am just displaying it on the screen
		 * */

		// Showing received message

		Toast.makeText(getApplicationContext(),
				"New Message: " + newMessage, Toast.LENGTH_LONG).show();

		// Releasing wake lock
		WakeLocker.release();
	}
};
@Override
protected void onDestroy() {
//	if (mRegisterTask != null) {
//		mRegisterTask.cancel(true);
//	}
	try {
		unregisterReceiver(mHandleMessageReceiver);
		GCMRegistrar.onDestroy(this);
	} catch (Exception e) {
		Log.e("UnRegister Receiver Error", "> " + e.getMessage());
	}

//	try {
//		mFileTemp.delete();
//	} catch (Exception ex) {
//		ex.printStackTrace();
//	}

	super.onDestroy();
}

}
