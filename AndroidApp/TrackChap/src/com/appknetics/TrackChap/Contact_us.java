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

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appknetics.TrackChap.R;
import com.fragments.Constantclass;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Contact_us extends Fragment implements OnClickListener {

	private EditText name,phone_number,issue,title;
	private Button contact_us;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private String title_edit;
	private String device_address;
	private int flag;
	String name_str,phone_str;
	private String status;
	public Contact_us(int flag) {
		// Required empty public constructor
		this.flag=flag;
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(flag==1){
			Bundle bundle=getArguments();		
			title_edit=bundle.getString("title_here");
			device_address=bundle.getString("device_address");
		}

	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		final TextView view = (TextView) getActivity().findViewById(R.id.clear);
		view.setVisibility(View.GONE);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		Constantclass.home_flag="0";

		View v= inflater.inflate(R.layout.fragment_contact_us, container, false);
		preferences=getActivity().getSharedPreferences("tracker",getActivity().MODE_PRIVATE);
		editor=preferences.edit();
		 name_str=preferences.getString("name","");
		 phone_str=preferences.getString("number","");
		Typeface confirm_font = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Book.otf");
		
		title=(EditText)v.findViewById(R.id.title);
		title.setText(title_edit);
		
		name=(EditText)v.findViewById(R.id.name);
		phone_number=(EditText)v.findViewById(R.id.phone_number);
		issue=(EditText)v.findViewById(R.id.issue);
		issue.setText(device_address);
		contact_us=(Button)v.findViewById(R.id.contact_us);
		name.setClickable(false);
		name.setFocusable(false);
		phone_number.setFocusable(false);
		
		title.setTypeface(confirm_font);
		name.setTypeface(confirm_font);
		phone_number.setTypeface(confirm_font);
		issue.setTypeface(confirm_font);
		contact_us.setTypeface(confirm_font);
		phone_number.setClickable(false);

		contact_us.setOnClickListener(this);
		name.setText(name_str);
		phone_number.setText(phone_str);
		if(flag==2){
			title.requestFocus();
		}
		return v;
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
				}
				Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();

			}else{
				new Contact_us_web().execute();
			}
			
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
				
				Toast.makeText(getActivity(), "Information send sucessfully.", Toast.LENGTH_SHORT).show();
				
			}else{
				Toast.makeText(getActivity(), "Web issues.", Toast.LENGTH_SHORT).show();

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
					.showOnlySpinningWheel(getActivity());
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
					Toast.makeText(getActivity(),"Web issues",Toast.LENGTH_LONG).show();
				}
		}
	}
}
