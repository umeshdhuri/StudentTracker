package com.appknetics.TrackChap;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appknetics.TrackChap.R;

public class Contactus_Admin extends Activity implements OnClickListener {
	private EditText name,phone_number,issue,title;
	private Button contact_us;
	private String status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contactus__admin);
		Typeface confirm_font = Typeface.createFromAsset(getAssets(), "Gotham-Book.otf");
		
		title=(EditText)findViewById(R.id.title);
		name=(EditText)findViewById(R.id.name);
		phone_number=(EditText)findViewById(R.id.phone_number);
		issue=(EditText)findViewById(R.id.issue);
		contact_us=(Button)findViewById(R.id.contact_us);
		contact_us.setOnClickListener(this);
		
		name.setTypeface(confirm_font);
		phone_number.setTypeface(confirm_font);
		issue.setTypeface(confirm_font);
		contact_us.setTypeface(confirm_font);
		title.setTypeface(confirm_font);

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.contact_us:
			String message=null;
			if(name.getText().toString().equals("")||phone_number.getText().toString().equals("")||title.getText().toString().equals("")||issue.getText().toString().equals("")){
				if(name.getText().toString().equals("")){
					message="Enter name.";
				}else if(phone_number.getText().toString().equals("")){
					message="Enter phone number.";
				}else if(title.getText().toString().equals("")){
					message="Enter title.";
				}else if(issue.getText().toString().equals("")){
					message="Enter issue.";
				}else{
					new Contact_us_web().execute();
				}
				Toast.makeText(Contactus_Admin.this,message, Toast.LENGTH_SHORT).show();
			}
			//new Contact_us_web().execute();
			break;

		default:
			break;
		}
	}
	public String Check_contact_us_info() {
		String jsonStrResp = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(ConstantUrlClass.CONTACT_US);
			List<NameValuePair> namevalpair = new ArrayList<NameValuePair>();
			namevalpair.add(new BasicNameValuePair("name",name.getText().toString().trim()));		
			namevalpair.add(new BasicNameValuePair("phone",phone_number.getText().toString().trim()));		
			namevalpair.add(new BasicNameValuePair("title",title.getText().toString().trim()));		
			namevalpair.add(new BasicNameValuePair("query",issue.getText().toString().trim()));
			httppost.setEntity(new UrlEncodedFormEntity(namevalpair));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			 jsonStrResp = httpclient.execute(httppost, responseHandler);
			 Log.d("==res","=="+jsonStrResp);
			return jsonStrResp;				
		}  catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	public String getResponse(String mystring2) {
		try {			
			Log.i("signUpresponse", "==" + mystring2);
			JSONObject JsonObj = new JSONObject(mystring2);
			  status=JsonObj.getString("status");
			//JSONObject JsonObjArr = JsonObj.getJSONObject("data");
			if (status.equalsIgnoreCase("true")) {
				
				Toast.makeText(Contactus_Admin.this, "Information send sucessfully.", Toast.LENGTH_SHORT).show();
				
			}else{
				Toast.makeText(Contactus_Admin.this, "Web issues.", Toast.LENGTH_SHORT).show();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	private class Contact_us_web extends AsyncTask<String, Void, Integer> {
		private ProgressDialog progressDialog;
		private String userResponse;
		@Override
		protected void onPreExecute() {
		
			progressDialog = DialogFactory
					.showOnlySpinningWheel(Contactus_Admin.this);
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(String... params) {
			
			userResponse=Check_contact_us_info();	
			Log.i("Login Activity", "==" + userResponse);
			return 1;
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
							
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				String a=getResponse(userResponse);		
				if(a.equals("false")){
					Toast.makeText(Contactus_Admin.this,"Web issues",Toast.LENGTH_LONG).show();
				}
		}
	}
	
}
