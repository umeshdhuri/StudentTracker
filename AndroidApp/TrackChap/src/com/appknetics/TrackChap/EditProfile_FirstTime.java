package com.appknetics.TrackChap;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.fragments.GMapV2Direction;
import com.fragments.LocationAddress;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint({ "HandlerLeak", "NewApi" })
public class EditProfile_FirstTime extends Activity implements
		LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, OnClickListener {
	private int arraysize;
	private LocationRequest lr;
	private LocationClient lc;
	private RelativeLayout map_lay;
	private MapView mapView;
	private GoogleMap map;
	static int flag = 0, progress_flag;
	private final long startTime = 16 * 1000;
	private final long interval = (long) (0.5 * 1000);
	private GMapV2Direction v2GetRouteDirection;
	private MarkerOptions markerOptions;
	private String androidId;
	private String status;
	private String savString;
	private int ii = 0;
	boolean ba = true;
	private Thread thread;
	// shared prefreances declration
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	Timer timer;
	TextView address, distances;
	TimerTask timerTask;
	// draw line constant
	private int a = 0, b = 1;
	Handler handler;
	private String user_latitude;
	private String user_longitude;
	private String end_latitude;
	private String end_longitude;
	private double current_lat;
	private double current_lang;
	Button edit,skip;
	private String address_fiedl;
	TextView info,title_head;
	private TextView address1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_profile__first_time);
		preferences = getSharedPreferences("tracker", MODE_PRIVATE);
		editor = preferences.edit();

		Typeface contact_us_font = Typeface.createFromAsset(getAssets(),
				"Gotham-Light.otf");
		Typeface info_font = Typeface.createFromAsset(getAssets(),"Gotham-Light.otf");
		
		info=(TextView) findViewById(R.id.info);
		info.setTypeface(info_font);
		
		Typeface head = Typeface.createFromAsset(getAssets(), "Gotham-Bold.otf");

		title_head=(TextView) findViewById(R.id.title_head);
		title_head.setTypeface(head);

//		ActionBar bar = getActionBar();
//		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
//		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
//
//		LayoutInflater mInflater = LayoutInflater.from(this);
//
//		View mCustomView = mInflater.inflate(R.layout.actionbar_layout, null);
//		bar.setCustomView(mCustomView);
//		
//		 address=(TextView)mCustomView.findViewById(R.id.address);
//		 address.setText("Update location");
//		 address.setTextColor(Color.parseColor("#0065ff"));
//		address.setTypeface(contact_us_font);

		
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		

		
		address = (TextView) findViewById(R.id.address);
		address1 = (TextView) findViewById(R.id.address1);

		address.setTypeface(contact_us_font);
		edit = (Button) findViewById(R.id.edit);
		edit.setTypeface(contact_us_font);
		edit.setOnClickListener(this);
		skip= (Button) findViewById(R.id.skip);
		skip.setTypeface(contact_us_font);
		skip.setOnClickListener(this);
		
		user_latitude = preferences.getString("user_latitude", "");
		user_longitude = preferences.getString("user_longitude", "");

		Log.d("==user_latitude==", "==" + user_latitude + "==" + user_longitude);

		end_latitude = preferences.getString("end_latitude", "");
		end_longitude = preferences.getString("end_longitude", "");
		Log.d("==end_latitude==", "==" + end_latitude + "==" + end_longitude);

		BitmapDescriptor i1 = BitmapDescriptorFactory
				.fromResource(R.drawable.track_icon_home);
		map.addMarker(new MarkerOptions()
				.position(
						new LatLng(Double.valueOf(user_latitude), Double
								.valueOf(user_longitude))).icon(i1)
				.title("Pick location"));
		CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(
				new LatLng(Double.valueOf(user_latitude), Double
						.valueOf(user_longitude)), 15);
		map.animateCamera(cameraUpdate1);

		LocationAddress locationAddress = new LocationAddress(1);
		locationAddress.getAddressFromLocation(Double.valueOf(user_latitude),
				Double.valueOf(user_longitude), EditProfile_FirstTime.this,
				new GeocoderHandler());

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		current_lat = location.getLatitude();
		current_lang = location.getLongitude();
	}

	public double getDistanceFromLatLonInKm(Double lat1, Double lon1,
			Double lat2, Double lon2) {
		int R = 6371; // Radius of the earth in km
		Double dLat = deg2rad(lat2 - lat1); // deg2rad below
		Double dLon = deg2rad(lon2 - lon1);
		Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		Double d = R * c; // Distance in km
		return d;
	}

	public double deg2rad(Double deg) {
		return deg * (Math.PI / 180);
	}

	private class GeocoderHandler extends Handler {

		@Override
		public void handleMessage(Message message) {
			String locationAddress = null;
			switch (message.what) {
			case 1:
				Bundle bundle = message.getData();
				locationAddress = bundle.getString("address");
				break;
			default:
				locationAddress = null;
			}
			Log.d("==locationAddress", "==" + locationAddress);
			address_fiedl = locationAddress;
			address.setText(locationAddress);
		}
	}

	public void dialogcalss(String message2) {

		final Dialog dialog = new Dialog(EditProfile_FirstTime.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout);
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText(message2);
		Button yes = (Button) dialog.findViewById(R.id.yes);
		Button no = (Button) dialog.findViewById(R.id.no);

		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map.clear();
				BitmapDescriptor i1 = BitmapDescriptorFactory
						.fromResource(R.drawable.start);
				map.addMarker(new MarkerOptions()
						.position(
								new LatLng(Double.valueOf(current_lat), Double
										.valueOf(current_lang))).icon(i1)
						.title("Pick location"));
				CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(
						new LatLng(Double.valueOf(current_lat), Double
								.valueOf(current_lang)), 15);
				map.animateCamera(cameraUpdate1);
				// address.setText(null);

				LocationAddress locationAddress = new LocationAddress(1);
				locationAddress.getAddressFromLocation(
						Double.valueOf(current_lat),
						Double.valueOf(current_lang),
						EditProfile_FirstTime.this, new GeocoderHandler());
				new Update_location().execute();
				// int flag=1;
				// Bundle bundle=new Bundle();
				// bundle.putString("title_here","Changed Address Request");
				// bundle.putString("device_address",address_fiedl);
				// FragmentManager contact_us
				// =getActivity().getSupportFragmentManager();
				// FragmentTransaction
				// transaction3=contact_us.beginTransaction();
				// Contact_us screen3=new Contact_us(flag);
				// screen3.setArguments(bundle);
				// transaction3.replace(R.id.frame_container,screen3);
				// transaction3.commit();
				//
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

	class Update_location extends AsyncTask<String, Integer, Void> {

		ProgressDialog progressDialog;
		private StringBuilder stringBuilder;
		private String res, status;
		private String id_reg;
		private String name;
		private String user_latitude;
		private String user_longitude;
		private String end_latitude;
		private String end_longitude;
		private String latitude_zero;
		private String longitude_one;
		String jsonStrResp = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = DialogFactory
					.showOnlySpinningWheel(EditProfile_FirstTime.this);
		}

		@Override
		protected Void doInBackground(String... params) {

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						ConstantUrlClass.Update_location);
				List<NameValuePair> namevalpair = new ArrayList<NameValuePair>();
				String uid = preferences.getString("id", "");

				namevalpair.add(new BasicNameValuePair("uid", uid));
				namevalpair.add(new BasicNameValuePair("latitude", String
						.valueOf(current_lat)));
				namevalpair.add(new BasicNameValuePair("longitude", String
						.valueOf(current_lang)));

				httppost.setEntity(new UrlEncodedFormEntity(namevalpair));
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				jsonStrResp = httpclient.execute(httppost, responseHandler);
				Log.d("==", "==" + jsonStrResp);
				// return jsonStrResp;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			try {
				JSONObject JsonObj = new JSONObject(jsonStrResp);
				status = JsonObj.getString("status");
				// JSONObject JsonObjArr = JsonObj.getJSONObject("data");
				if (status.equalsIgnoreCase("true")) {
					Log.d("==", "==" + true);
				}
			} catch (Exception e) {
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.edit:
			dialogcalss("Are you sure you want to change the address");
			break;
		case R.id.skip:
			Intent i=new Intent(EditProfile_FirstTime.this,HomeActivity.class);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
	}
}
