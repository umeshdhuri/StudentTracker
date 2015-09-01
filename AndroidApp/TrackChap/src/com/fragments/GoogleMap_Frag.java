package com.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appknetics.TrackChap.ConnectionDetector;
import com.appknetics.TrackChap.ConstantUrlClass;
import com.appknetics.TrackChap.DialogFactory;
import com.appknetics.TrackChap.HomeActivity;
import com.appknetics.TrackChap.Login_Activity;
import com.appknetics.TrackChap.R;
import com.appknetics.TrackChap.Second;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ActionProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "HandlerLeak", "NewApi" }) public class GoogleMap_Frag extends Fragment implements OnClickListener,
		LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	
	private int arraysize;
	private LocationRequest lr;
	private LocationClient lc;
	private RelativeLayout map_lay;
	private MapView mapView;
	private GoogleMap map;
	static int flag = 0, progress_flag;
	private final long startTime = 16 * 1000;
	private final long interval = (long) (0.5 * 1000);
	private MarkerOptions markerOptions;
	private String androidId;
	private String status;
	private String savString;
	private int ii = 0;
	boolean ba = true;
	private Thread thread;
	// sharedprefreances declration
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	Timer timer;	
	TextView address,distances,hometoany,map_,Hybrid,Satellite;
	TimerTask timerTask;
	// draw line constant
	private  int a = 0, b = 1;
	 Handler handler;
	private String user_latitude;
	private String user_longitude;
	private String end_latitude;
	private String end_longitude;
	private String which_way;
	private double km_Second;
	private double km_first;
	private Marker current_location;
	private GroundOverlay current_location1;

	private Menu menu;
	private int i = 0;
	  int aa=0,bb=1;
	  private boolean isZooming = false;
	  private float previousZoomLevel = -1.0f;
//--------------------------constructor is here ---------------------------	
	public GoogleMap_Frag() {
		// Required empty public constructor
		  handler = new Handler();
	}
	
//------------------------------------timer goes here----------------------------------------------------------------------	
	public void startTimer() {		
			        //set a new Timer
			        timer = new Timer();
			        //initialize the TimerTask's job
		
			        initializeTimerTask();
			        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
//			        task  the task to schedule. 
//			        delay  amount of time in milliseconds before first execution. 
//			        period  amount of time in milliseconds between subsequent executions. 
			        timer.schedule(timerTask, 500, 1000); //
		
			    }

	public void initializeTimerTask() {
		timerTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					@SuppressLint("NewApi") public void run() {
						
						
	
//							if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
//								new TrackStudent(ii)
//										.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//							} else {
								new TrackStudent(ii).execute();
							//}
					}
				});
			}
		};
	}

	public void stoptimertask() {

		// stop the timer, if it's not already null

		if (timer != null) {

			timer.cancel();

			timer = null;

		}

	}
//----------------------------------------------------------------------------------------------------------	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		final TextView view = (TextView) getActivity().findViewById(R.id.clear);
		view.setVisibility(View.GONE);
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Constantclass.home_flag="1";
		View v = inflater.inflate(R.layout.google_map_, container, false);
		address=(TextView)v.findViewById(R.id.address);
		distances=(TextView)v.findViewById(R.id.distances);
		map_=(TextView)v.findViewById(R.id.map_);
		Hybrid=(TextView)v.findViewById(R.id.Hybrid);
		Satellite=(TextView)v.findViewById(R.id.Satellite);
		hometoany=(TextView)v.findViewById(R.id.hometoany);
		map_lay = (RelativeLayout) v.findViewById(R.id.map_lay);
		mapView = (MapView) v.findViewById(R.id.mapview);
		
		//custome font
		Typeface address_font = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Book.otf");
		Typeface hometoany_font = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Light.otf");

		
		//distances
		distances.setTypeface(address_font);
		//which way eg. school to home or....
		hometoany.setTypeface(hometoany_font);		
		//address 
		address.setTypeface(hometoany_font);
		
		
		map_.setTypeface(hometoany_font);
		Hybrid.setTypeface(hometoany_font);
		Satellite.setTypeface(hometoany_font);
		//starting fill map background
		map_.setBackgroundColor(Color.parseColor("#007aff"));
		
		//button listner
		map_.setOnClickListener(this);
		Hybrid.setOnClickListener(this);
		Satellite.setOnClickListener(this);
		
		//get end lat and long
		user_latitude = preferences.getString("user_latitude", "");
		user_longitude = preferences.getString("user_longitude", "");
		Log.d("==user_latitude==","=="+user_latitude+"=="+user_longitude);
		 
		//get start lat and long
		end_latitude = preferences.getString("end_latitude", "");
		end_longitude = preferences.getString("end_longitude", "");
		Log.d("==end_latitude==","=="+end_latitude+"=="+end_longitude);
 		 
		//get the android device id
		androidId = Secure.getString(getActivity().getContentResolver(),
				Secure.ANDROID_ID);

		//create the object of marker options
		markerOptions = new MarkerOptions();

		//initalization or add the map to map view
		callMap(v, savedInstanceState);

		//====================== add the end point in map view ===========================
		BitmapDescriptor i1 = BitmapDescriptorFactory
				.fromResource(R.drawable.track_icon_home);
		map.addMarker(new MarkerOptions()
				.position(
						new LatLng(Double.valueOf(user_latitude.toString()), Double
								.valueOf(user_longitude.toString()))).icon(i1).flat(true)
				.title("School"));
		
		
//		 BitmapDescriptor image1 = BitmapDescriptorFactory.fromResource(R.drawable.track_icon_home);
//		 
//		 GroundOverlayOptions groundOverlay1 = new GroundOverlayOptions()
//         .image(image1)
//         .position(
//					new LatLng(Double.valueOf(user_latitude.toString()), Double
//							.valueOf(user_longitude.toString())),500f)
//         ;
//		
//         map.addGroundOverlay(groundOverlay1);
      //   map.setOnCameraChangeListener(getCameraChangeListener());

         
		
		
		CameraUpdate cameraUpdate1 = CameraUpdateFactory.newLatLngZoom(
				new LatLng(Double.valueOf(user_latitude.toString()), Double
						.valueOf(user_longitude.toString())), 14);
		map.animateCamera(cameraUpdate1);

		
		//====================== add the Start point in map view ===========================
		BitmapDescriptor i2 = BitmapDescriptorFactory
				.fromResource(R.drawable.track_icon_school);
		
		map.addMarker(new MarkerOptions()
				.position(
						new LatLng(Double.valueOf(end_latitude.toString()), Double
								.valueOf(end_longitude.toString()))).icon(i2).flat(true)
				.title("Home"));
		
//		 BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.track_icon_school);
//         GroundOverlayOptions groundOverlay = new GroundOverlayOptions()
//         .image(image)
//         .position(
//					new LatLng(Double.valueOf(end_latitude.toString()), Double
//							.valueOf(end_longitude.toString())), 500f)
//         ;
//		
//         map.addGroundOverlay(groundOverlay);
         
	
         CameraUpdate cameraUpdate2 = CameraUpdateFactory.newLatLngZoom(
				new LatLng(Double.valueOf(end_latitude.toString()), Double
						.valueOf(end_longitude.toString())), 14);
		map.animateCamera(cameraUpdate2);

		
		
		//calculate the distances between the start and end point
		double	km =getDistanceFromLatLonInKm(Double.valueOf(user_latitude.toString()), Double
								.valueOf(user_longitude.toString()),Double.valueOf(end_latitude.toString()), Double
								.valueOf(end_longitude.toString()));
	
		//formula for calculating time using kilometer 
		float time=(float) ((km/30)*60);
		//get time in 3.22 like we get the digit befor dot and after dot
		StringTokenizer tokens = new StringTokenizer(String.valueOf(time), ".");
		String firststring = tokens.nextToken();
		String secondstring = tokens.nextToken();
		Log.d("==secondstring","==="+secondstring);
		String upToNCharacters = secondstring.substring(0, 2);
		//check if secondstring is grater then 60 then put value 60
		if(Integer.parseInt(upToNCharacters)>60){
			upToNCharacters="60";
		}
		//	distances.setText(firststring+" min "+String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(secondstring)))+"sec "+"("+String.valueOf(new DecimalFormat("##.##").format(km))+"km)");
	
	//================== if getcordinate contains lat and long then draw path directly without timer and not the start the timer =========
	
		//also check internet connectivty
		if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
			
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//				new FirstDraw()
//						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//			} else {
				new FirstDraw().execute();
			//}
			
			
		} else {
			Toast.makeText(getActivity(), "No internet connectivity.",
					Toast.LENGTH_SHORT).show();
		}
		
		
		
//=============================== get array testing only  ======================================
//if (new ConnectionDetector(getActivity()).isConnectingToInternet()) {
//			
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//				new GetAll()
//						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//			} else {
//				new GetAll().execute();
//			}
//			
//			
//		} else {
//			Toast.makeText(getActivity(), "No internet connectivity.",
//					Toast.LENGTH_SHORT).show();
//		}
//========================================================================================================
		
		return v;
	}
	public OnCameraChangeListener getCameraChangeListener()
	{
	    return new OnCameraChangeListener() 
	    {
	       
			@Override
			public void onCameraChange(CameraPosition position) {
				// TODO Auto-generated method stub
				Log.d("Zoom", "Zoom: " + position.zoom);

			
				
	            if(previousZoomLevel != position.zoom)
	            {
	                isZooming = true;
	            }

	            previousZoomLevel = position.zoom;
			}
	    };
	}

//--------------------------------  click events -----------------------------------------------------
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.accept_it:
			break;

		case R.id.map_:
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			//setbackground color
			map_.setBackgroundColor(Color.parseColor("#007aff"));
			Hybrid.setBackgroundColor(Color.parseColor("#ffffff"));
			Hybrid.setBackgroundResource(R.drawable.middle);
			Satellite.setBackgroundResource(R.drawable.right);

			//set textcolor white
			map_.setTextColor(Color.parseColor("#ffffff"));
			Hybrid.setTextColor(Color.parseColor("#000000"));
			Satellite.setTextColor(Color.parseColor("#000000"));
			break;
		case R.id.Hybrid:
			map.setMapType(GoogleMap.MAP_TYPE_HYBRID);	
			
			//setbackground color
			map_.setBackgroundResource(R.drawable.left);
			Hybrid.setBackgroundColor(Color.parseColor("#007aff"));
			Satellite.setBackgroundResource(R.drawable.right);
			
			//set textcolor white
			map_.setTextColor(Color.parseColor("#000000"));
			Hybrid.setTextColor(Color.parseColor("#ffffff"));
			Satellite.setTextColor(Color.parseColor("#000000"));
			break;
		case R.id.Satellite:
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);			

			//setbackground color
			Hybrid.setBackgroundResource(R.drawable.middle);
			map_.setBackgroundResource(R.drawable.left);
			Satellite.setBackgroundColor(Color.parseColor("#007aff"));

			//set textcolor white
			map_.setTextColor(Color.parseColor("#000000"));
			Hybrid.setTextColor(Color.parseColor("#000000"));
			Satellite.setTextColor(Color.parseColor("#ffffff"));
			break;
		default:
			break;
		}
	}
//--------------------------------------------------------------------------------------------------
	public void callMap(View v, Bundle savedInstanceState) {
		mapView.onCreate(savedInstanceState);
		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(false);
		try {
			MapsInitializer.initialize(this.getActivity());
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}

	}
//--------------------------------------------------------------------------------------------------
	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// latt=location.getLatitude();
		// longt=location.getLongitude();

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		lc.requestLocationUpdates(lr, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}
//--------------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		preferences = getActivity().getSharedPreferences("tracker",
				getActivity().MODE_PRIVATE);
		editor = preferences.edit();

		

		lr = LocationRequest.create();
		lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		lc = new LocationClient(this.getActivity().getApplicationContext(),
				this, this);
		lc.connect();
	}
//--------------------------------------------------------------------------------------------------

	class TrackStudent extends AsyncTask<String, Integer, Void> {

		ProgressDialog progressDialog;
		private StringBuilder stringBuilder;
		private String res, status;
		private String id_reg;
		private String name;
		private String user_latitude;
		private String user_longitude;
		private String end_latitude;
		private String end_longitude;
		private String id;
		private int i;
		private String id_track;
		private String latitude_track;
		private String longitude_track;
		private Marker privous_location;
		Double previous_lat= null,dprevious_long = null;
		private double km_get_inside;
		private double km_first_inside;
		private double km_Second_inside;
		
		public TrackStudent(int i) {
			// TODO Auto-generated constructor stub
			this.i = i;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();			
		}

		@Override
		protected Void doInBackground(String... params) {
			
			String id_res = preferences.getString("id", "");
			HttpClient httpclient = new DefaultHttpClient();
			
			
			//testing phase only			
//			HttpGet httpGet = new HttpGet(ConstantUrlClass.Track_student
//					+ "?uid=" + id_res + "&limit=" + i);
			
			//development phase
			
			HttpGet httpGet = new HttpGet(ConstantUrlClass.Track_student
					+ "?uid=" + id_res);
			

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
			// progressDialog.dismiss();

			try {
				JSONObject obj = new JSONObject(res);
				status = obj.getString("status");


				
				if (status.equals("true")) {
					JSONArray data = obj.getJSONArray("data");

					
				if (data.length() > 1) {
						
					Polyline line = map
							.addPolyline(new PolylineOptions()
									.add(new LatLng(
											Double.parseDouble(data
													.getJSONObject(data.length()-2)
													.getString("latitude").toString()),
											Double.parseDouble(data
													.getJSONObject(data.length()-2)
													.getString("longitude").toString())),
											new LatLng(
													Double.parseDouble(data
															.getJSONObject(data.length()-1)
															.getString(
																	"latitude").toString()),
													Double.parseDouble(data
															.getJSONObject(data.length()-1)
															.getString(
																	"longitude").toString())))
									.width(8).color(Color.BLUE).geodesic(true)
									.visible(true));
					
					
					
					if((current_location1==null))
					{
						BitmapDescriptor i2 = BitmapDescriptorFactory
								.fromResource(R.drawable.track);						
//						  LatLng mapCenter = new LatLng(Double.parseDouble(data
//									.getJSONObject(b)
//									.getString(
//											"latitude").toString()), Double.parseDouble(data
//													.getJSONObject(b)
//													.getString(
//															"longitude").toString()));
//
//						 
//						  
//						  
//						  current_location = map.addMarker(new MarkerOptions()
//								.position(
//										new LatLng(Double.parseDouble(data
//												.getJSONObject(b)
//												.getString(
//														"latitude").toString()), Double.parseDouble(data
//																.getJSONObject(b)
//																.getString(
//																		"longitude").toString()))).icon(i2).flat(true).position(mapCenter)
//								.title("Current location"));
						  
						  
						  BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.track);
			                 GroundOverlayOptions groundOverlay = new GroundOverlayOptions()
			                 .image(image)
			                 .position(
										new LatLng(Double.parseDouble(data
												.getJSONObject(b)
												.getString(
														"latitude").toString()), Double.parseDouble(data
																.getJSONObject(b)
																.getString(
																		"longitude").toString())), 160f)
			                 ;
			        		
			                 current_location1 =  map.addGroundOverlay(groundOverlay);
						  
						
					}
					if(current_location1!=null)
					{
						
//						BitmapDescriptor i2 = BitmapDescriptorFactory
//								.fromResource(R.drawable.track);
						
		        		 BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.track);
		                 GroundOverlayOptions groundOverlay = new GroundOverlayOptions()
		                 .image(image)
		                 .position(
									new LatLng(Double.parseDouble(data
											.getJSONObject(b)
											.getString(
													"latitude").toString()), Double.parseDouble(data
															.getJSONObject(b)
															.getString(
																	"longitude").toString())), 160f)
		                 ;
		                 current_location1.remove();
		                 current_location1 = map.addGroundOverlay(groundOverlay);
						
						
//						  LatLng mapCenter = new LatLng(Double.parseDouble(data
//									.getJSONObject(b)
//									.getString(
//											"latitude").toString()), Double.parseDouble(data
//													.getJSONObject(b)
//													.getString(
//															"longitude").toString()));
//
//						current_location = map.addMarker(new MarkerOptions()
//								.position(
//										new LatLng(Double.parseDouble(data
//												.getJSONObject(b)
//												.getString(
//														"latitude").toString()), Double.parseDouble(data
//																.getJSONObject(b)
//																.getString(
//																		"longitude").toString()))).icon(i2).flat(true).position(mapCenter)
//								.title("Current location"));
					}
					//==============remove old cuurent loaction and add new loaction ===========================================================================			
					
//					if((current_location==null))
//					{
//						BitmapDescriptor i2 = BitmapDescriptorFactory
//								.fromResource(R.drawable.track);						
//						
//						current_location = map.addMarker(new MarkerOptions()
//								.position(
//										new LatLng(Double.parseDouble(data
//												.getJSONObject(data.length()-1)
//												.getString(
//														"latitude").toString()), Double.parseDouble(data
//																.getJSONObject(data.length()-1)
//																.getString(
//																		"longitude").toString()))).icon(i2)
//								.title("Current location"));
//						
//					}
//					if(current_location!=null){
//						current_location.remove();
//						BitmapDescriptor i2 = BitmapDescriptorFactory
//								.fromResource(R.drawable.track);
//						
//						
//						current_location = map.addMarker(new MarkerOptions()
//								.position(
//										new LatLng(Double.parseDouble(data
//												.getJSONObject(data.length()-1)
//												.getString(
//														"latitude").toString()), Double.parseDouble(data
//																.getJSONObject(data.length()-1)
//																.getString(
//																		"longitude").toString()))).icon(i2)
//								.title("Current location"));
//					}
		//=====================================			
					
					//get the 3rd lat and long value from json array
					Double third_lat = Double.parseDouble(data
							.getJSONObject(1)
							.getString("latitude").toString());
					Double third_lang = Double.parseDouble(data
							.getJSONObject(1)
							.getString("longitude").toString());
					
					//start lat and long					
					 end_latitude = preferences.getString("end_latitude", "");
					 end_longitude = preferences.getString("end_longitude", "");
					 Double end_latitude_a = Double.parseDouble(end_latitude.toString());
					 Double end_longitude_b = Double.parseDouble(end_longitude.toString());

					 //get km for start lat and json array 3 rd point
					km_first_inside =getDistanceFromLatLonInKm(third_lat,third_lang,end_latitude_a,end_longitude_b);
					
						
					//end lat and long	
					user_latitude = preferences.getString("user_latitude", "");
					user_longitude = preferences.getString("user_longitude", "");
					Double user_latitude_a = Double.parseDouble(user_latitude.toString());
					Double user_longitude_b = Double.parseDouble(user_longitude.toString());
					
					  //get km in end lat and long and 3rd point
					km_Second_inside =getDistanceFromLatLonInKm(third_lat,third_lang,user_latitude_a,user_longitude_b);

					  Log.d("==km_first","==="+km_first_inside);
					  Log.d("==km_Second","==="+km_Second_inside);
					 
					 // if km_first less the km_Second  the school to home else home to school
					if(km_first_inside<km_Second_inside){
						hometoany.setText("School to Home ");
						which_way="home";
						//km_get_inside=km_Second;
						Double last_lat = Double.parseDouble(data
								.getJSONObject(data.length()-1)
								.getString("latitude").toString());
						Double last_long = Double.parseDouble(data
								.getJSONObject(data.length()-1)
								.getString("longitude").toString());
						km_get_inside =getDistanceFromLatLonInKm(last_lat,last_long,user_latitude_a,user_longitude_b);
						Log.d("==","==--=="+"end====="+km_get_inside);
						
					}else{						
						hometoany.setText("Home to school");
						which_way="school";
						//km_get_inside=km_first;
						Double last_lat = Double.parseDouble(data
								.getJSONObject(data.length()-1)
								.getString("latitude").toString());
						Double last_long = Double.parseDouble(data
								.getJSONObject(data.length()-1)
								.getString("longitude").toString());
						km_get_inside =getDistanceFromLatLonInKm(last_lat,last_long,end_latitude_a,end_longitude_b);
						Log.d("==","==--=="+"start====="+km_get_inside);
					}
					
				//==================  add the polyline in google mapview ==================
						
						
					
						// for getting start and end lat and long because the find out the address.
						//start lat
						 end_latitude = preferences.getString("end_latitude", "");
						 end_longitude = preferences.getString("end_longitude", "");
						 Log.d("==value of end_latitude & end_latitude", "==" + end_latitude + "===" + end_longitude);
						 //end lat 
						 user_latitude = preferences.getString("user_latitude", "");
						 user_longitude = preferences.getString("user_longitude", "");
						 
						 //find out the address of last value in json array
						 LocationAddress locationAddress = new LocationAddress(2);
					        locationAddress.getAddressFromLocation(Double.parseDouble(data
									.getJSONObject(data.length()-1)
									.getString(
											"latitude").toString()), Double.parseDouble(data
													.getJSONObject(data.length()-1)
													.getString(
															"longitude").toString()),
					                getActivity(), new GeocoderHandler());
						 
						 //find out the distances between the last point and last point in json array
//						double	km =getDistanceFromLatLonInKm(
//								Double.parseDouble(user_latitude.toString()),
//								Double.parseDouble(user_longitude.toString()),
//										Double.parseDouble(data
//												.getJSONObject(b)
//												.getString(
//														"latitude").toString()),
//										Double.parseDouble(data
//												.getJSONObject(b)
//												.getString(
//														"longitude").toString()));
					
						
						
	
						
//=================================================================================================================================										
						
						float time=(float) ((km_get_inside/30)*60);
						StringTokenizer tokens = new StringTokenizer(String.valueOf(time), ".");
						String firststring = tokens.nextToken();
						String secondstring = tokens.nextToken();
						Log.d("==secondstring web","==="+secondstring);
						String upToNCharacters = secondstring.substring(0, 2);

						if(Integer.parseInt(upToNCharacters)>60){
							upToNCharacters="60";
						}
						distances.setText(firststring+" min "+String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(upToNCharacters.toString())))+" sec "+"("+String.valueOf(new DecimalFormat("##.##").format(km_get_inside))+" km)");

						Log.d("==kmrange","=------="+km_get_inside);
						
						if(km_get_inside <=0.100000){
							//stoptimertask();
							//a=0;b=1;
							
							//new ReachHome().execute();
							
							
							hometoany.setText("");
							if(km_first_inside<km_Second_inside){
								hometoany.setText("Student Reached Home");
								//which_way="home";
							}else{						
								hometoany.setText("Student Reached School");
							//	which_way="school";
							}
							
							//hometoany.setText("Student Reached Home");
							
						}
						a++;
						b++;
						Log.d("==value of a & b", "==" + a + "===" + b);
						
					}
				
				
			}else {
					Toast.makeText(getActivity(), "Invalid mobile number",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			ii++;
		}
	}

//---------------------------------------------------------------------------------------------------------------------------
	
	//calculate the distances between two lat and long
	public double getDistanceFromLatLonInKm(Double lat1,Double lon1,Double lat2,Double lon2) {
		  int R = 6371; // Radius of the earth in km
		  Double dLat = deg2rad(lat2-lat1);  // deg2rad below
		  Double dLon = deg2rad(lon2-lon1); 
		  Double a = 
		    Math.sin(dLat/2) * Math.sin(dLat/2) +
		    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
		    Math.sin(dLon/2) * Math.sin(dLon/2)
		    ; 
		  Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		  Double d = R * c; // Distance in km
		  return d;
		}
	public  double deg2rad(Double deg) {
		  return deg * (Math.PI/180);
		}
	//get the address for perticular lat and long
	private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress=null;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            Log.d("==locationAddress","=="+locationAddress);
            address.setText("via: "+locationAddress);
        }
    }
	//==================student reach home then call this web service=================================
	
	class ReachHome extends AsyncTask<String,Integer,Void>{

		ProgressDialog progressDialog;
		private StringBuilder stringBuilder;
		private String res,status;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = DialogFactory
					.showOnlySpinningWheel(getActivity());
		}
		
		@Override
		protected Void doInBackground(String... params) {
			String	regid = preferences.getString("regid", "");
			String	uid = preferences.getString("id", "");

			HttpClient httpclient = new DefaultHttpClient();
		//	uid=10&wayto=home_OR_school
			
			HttpGet httpGet = new HttpGet(ConstantUrlClass.ReachHome+"uid="+uid+"&wayto="+which_way);
			Log.d("==url","=--="+ConstantUrlClass.ReachHome+"uid="+uid+"&wayto="+which_way);
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
					Toast.makeText(getActivity(), "Reach Home",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getActivity(), "Error in Web",Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
			}
			
		}
	}

//=================================== switch to other activity then first draw path and later start timer ===================
	
	class FirstDraw extends AsyncTask<String, Integer, Void> {

		ProgressDialog progressDialog;
		private StringBuilder stringBuilder;
		private String res, status;
			private int i;
			int aa=0,bb=1;
			double	km_get;
		Double previous_lat= null,dprevious_long = null;
		private double km_get_last;
		private double km_get_first;
		public FirstDraw() {
			// TODO Auto-generated constructor stub
			//this.i = i;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			 progressDialog = DialogFactory
			 .showOnlySpinningWheel(getActivity());
		}

		@Override
		protected Void doInBackground(String... params) {
			
			String id_res = preferences.getString("id", "");
			Log.d("==id_res","==--"+id_res);
			HttpClient httpclient = new DefaultHttpClient();
			// phone_str=phone1.getText().toString().trim();
			HttpGet httpGet = new HttpGet(ConstantUrlClass.Track_student
					+ "?uid=" +id_res);
		//	Log.d("==urlll", "==" + ConstantUrlClass.Track_student + "?uid="+ id_res);
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
			 
			//start
			end_latitude = preferences.getString("end_latitude", "");
			 end_longitude = preferences.getString("end_longitude", "");
			 Log.d("==value of end_latitude & end_latitude", "==" + end_latitude + "===" + end_longitude);
			//end 
			 user_latitude = preferences.getString("user_latitude", "");
			 user_longitude = preferences.getString("user_longitude", "");
			 
			try {
				JSONObject obj = new JSONObject(res);
				status = obj.getString("status");			
				if (status.equals("true")) {
					JSONArray data = obj.getJSONArray("data");
					Log.d("==data.length()","===--"+data.length());

					if(data.length()>1){
						
						
					for(int i=0;i<data.length()-1;i++){
					
						// get the 3rd point from json array
						Double third_lat = Double.parseDouble(data
								.getJSONObject(1)
								.getString("latitude").toString());
						Double third_lang = Double.parseDouble(data
								.getJSONObject(1)
								.getString("longitude").toString());
						Log.d("==third_lat","===--"+third_lat);
						Log.d("==third_lang","===--"+third_lang);

						//start
						 end_latitude = preferences.getString("end_latitude", "");
						 end_longitude = preferences.getString("end_longitude", "");
						 Double end_latitude_a = Double.parseDouble(end_latitude.toString());
						 Double end_longitude_b = Double.parseDouble(end_longitude.toString());
						 km_first =getDistanceFromLatLonInKm(third_lat,third_lang,end_latitude_a,end_longitude_b);
						 Log.d("==km_first","===--"+km_first);

						//end
						user_latitude = preferences.getString("user_latitude", "");
						user_longitude = preferences.getString("user_longitude", "");						 
						Double user_latitude_a = Double.parseDouble(user_latitude.toString());
						Double user_longitude_b = Double.parseDouble(user_longitude.toString());						
						km_Second =getDistanceFromLatLonInKm(third_lat,third_lang,user_latitude_a,user_longitude_b);
						Log.d("==km_Second","===--=="+km_Second);
						
						//check which way going
						if(km_first < km_Second){
							Log.d("==km_Second","===--"+"School to Home ");

							
							hometoany.setText("School to Home ");
							which_way="home";
							//km_get=km_Second;
							Double last_lat = Double.parseDouble(data
									.getJSONObject(b)
									.getString("latitude").toString());
							Double last_long = Double.parseDouble(data
									.getJSONObject(b)
									.getString("longitude").toString());
							km_get =getDistanceFromLatLonInKm(last_lat,last_long,user_latitude_a,user_longitude_b);
							Log.d("==end km_get ","===--"+"end====="+km_get);

						}else{
							Log.d("==km_Second","===--"+"Home to school");

							hometoany.setText("Home to school");
							which_way="school";
							//km_get=km_first;
							Double last_lat = Double.parseDouble(data
									.getJSONObject(b)
									.getString("latitude").toString());
							Double last_long = Double.parseDouble(data
									.getJSONObject(b)
									.getString("longitude").toString());
							km_get =getDistanceFromLatLonInKm(last_lat,last_long,end_latitude_a,end_longitude_b);
							Log.d("==km_get start","===--"+"start====="+km_get);
						}
						
					//draw polyline in google mapview	
						
						
						Polyline line = map
								.addPolyline(new PolylineOptions()
										.add(new LatLng(
												Double.parseDouble(data
														.getJSONObject(a)
														.getString("latitude").toString()),
												Double.parseDouble(data
														.getJSONObject(a)
														.getString("longitude").toString())),
												new LatLng(
														Double.parseDouble(data
																.getJSONObject(b)
																.getString(
																		"latitude").toString()),
														Double.parseDouble(data
																.getJSONObject(b)
																.getString(
																		"longitude").toString())))
										.width(8).color(Color.BLUE)
										.visible(true));	
						
						if((current_location1==null))
						{
							BitmapDescriptor i2 = BitmapDescriptorFactory
									.fromResource(R.drawable.track);						
//							  LatLng mapCenter = new LatLng(Double.parseDouble(data
//										.getJSONObject(b)
//										.getString(
//												"latitude").toString()), Double.parseDouble(data
//														.getJSONObject(b)
//														.getString(
//																"longitude").toString()));
	//
//							 
//							  
//							  
//							  current_location = map.addMarker(new MarkerOptions()
//									.position(
//											new LatLng(Double.parseDouble(data
//													.getJSONObject(b)
//													.getString(
//															"latitude").toString()), Double.parseDouble(data
//																	.getJSONObject(b)
//																	.getString(
//																			"longitude").toString()))).icon(i2).flat(true).position(mapCenter)
//									.title("Current location"));
							  
							  
							  BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.track);
				                 GroundOverlayOptions groundOverlay = new GroundOverlayOptions()
				                 .image(image)
				                 .position(
											new LatLng(Double.parseDouble(data
													.getJSONObject(b)
													.getString(
															"latitude").toString()), Double.parseDouble(data
																	.getJSONObject(b)
																	.getString(
																			"longitude").toString())), 160f)
				                 ;
				        		
				                 current_location1 =  map.addGroundOverlay(groundOverlay);
							  
							
						}
						if(current_location1!=null)
						{
							
//							BitmapDescriptor i2 = BitmapDescriptorFactory
//									.fromResource(R.drawable.track);
							
			        		 BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.track);
			                 GroundOverlayOptions groundOverlay = new GroundOverlayOptions()
			                 .image(image)
			                 .position(
										new LatLng(Double.parseDouble(data
												.getJSONObject(b)
												.getString(
														"latitude").toString()), Double.parseDouble(data
																.getJSONObject(b)
																.getString(
																		"longitude").toString())), 160f)
			                 ;
			                 current_location1.remove();
			                 current_location1 = map.addGroundOverlay(groundOverlay);
							
							
//							  LatLng mapCenter = new LatLng(Double.parseDouble(data
//										.getJSONObject(b)
//										.getString(
//												"latitude").toString()), Double.parseDouble(data
//														.getJSONObject(b)
//														.getString(
//																"longitude").toString()));
	//
//							current_location = map.addMarker(new MarkerOptions()
//									.position(
//											new LatLng(Double.parseDouble(data
//													.getJSONObject(b)
//													.getString(
//															"latitude").toString()), Double.parseDouble(data
//																	.getJSONObject(b)
//																	.getString(
//																			"longitude").toString()))).icon(i2).flat(true).position(mapCenter)
//									.title("Current location"));
						}
						 
						 //find the address last location json array 
//						 LocationAddress locationAddress = new LocationAddress(2);
//					        locationAddress.getAddressFromLocation(Double.parseDouble(data
//									.getJSONObject(b)
//									.getString(
//											"latitude").toString()), Double.parseDouble(data
//													.getJSONObject(b)
//													.getString(
//															"longitude").toString()),
//					                getActivity(), new GeocoderHandler());
						 
					        //find out the last lat and long in json array and the end point
							
//==============remove old cuurent loaction and add new loaction ===========================================================================			
						
//						if((current_location==null))
//						{
//							BitmapDescriptor i2 = BitmapDescriptorFactory
//									.fromResource(R.drawable.track);						
//							
//							current_location = map.addMarker(new MarkerOptions()
//									.position(
//											new LatLng(Double.parseDouble(data
//													.getJSONObject(b)
//													.getString(
//															"latitude").toString()), Double.parseDouble(data
//																	.getJSONObject(b)
//																	.getString(
//																			"longitude").toString()))).icon(i2)
//									.title("Current location"));
//							
//						}
//						if(current_location!=null){
//							current_location.remove();
//							BitmapDescriptor i2 = BitmapDescriptorFactory
//									.fromResource(R.drawable.track);
//							
//							
//							current_location = map.addMarker(new MarkerOptions()
//									.position(
//											new LatLng(Double.parseDouble(data
//													.getJSONObject(b)
//													.getString(
//															"latitude").toString()), Double.parseDouble(data
//																	.getJSONObject(b)
//																	.getString(
//																			"longitude").toString()))).icon(i2)
//									.title("Current location"));
//						}
						
//=================================================================================================================================		
						
						
						a++;
						b++;
						}
					 progressDialog.dismiss();
					 //find the address last location json array 
					 LocationAddress locationAddress = new LocationAddress(2);
				        locationAddress.getAddressFromLocation(Double.parseDouble(data
								.getJSONObject(b-1)
								.getString(
										"latitude").toString()), Double.parseDouble(data
												.getJSONObject(b-1)
												.getString(
														"longitude").toString()),
				                getActivity(), new GeocoderHandler());
					 
					 
					 end_latitude = preferences.getString("end_latitude", "");
					 end_longitude = preferences.getString("end_longitude", "");
					 Log.d("==value of end_latitude & end_latitude", "==" + end_latitude + "===" + end_longitude);
					//end 
					 user_latitude = preferences.getString("user_latitude", "");
					 user_longitude = preferences.getString("user_longitude", "");
						
					float time=(float) ((km_get/30)*60);
					StringTokenizer tokens = new StringTokenizer(String.valueOf(time), ".");
					String firststring = tokens.nextToken();
					String secondstring = tokens.nextToken();
					Log.d("==secondstring web","==="+secondstring);
					String upToNCharacters = secondstring.substring(0, 2);

					if(Integer.parseInt(upToNCharacters)>60){
						upToNCharacters="60";
					}
					distances.setText(firststring+" min "+String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(upToNCharacters.toString())))+" sec "+"("+String.valueOf(new DecimalFormat("##.##").format(km_get))+" km)");
					Log.d("==kmrange","=="+km_first);
					
					
					//if distance is less than 0.1 then hit reachHome webservice and stop timer
					if(km_get <=0.100000){
						//stoptimertask();
					//	a=0;b=1;
						
						//new ReachHome().execute();
						
						
						hometoany.setText("");
						if(km_first<km_Second){
							hometoany.setText("Student Reached Home");
						}else{						
							hometoany.setText("Student Reached School");
						}
						progressDialog.dismiss();
					}else{
						progressDialog.dismiss();
						// draw path for eg. 1 to 10 cordinate and after 10 to home draw using timer
						startTimer();
					}
					startTimer();
					}
					else{
						progressDialog.dismiss();
						//if getcordinate array is empty then draw route from starting point
						startTimer();
					}
					
				}else {
					progressDialog.dismiss();
					Toast.makeText(getActivity(), "Web issue",
							Toast.LENGTH_SHORT).show();
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("==getMessage","=---="+e.getMessage());
			}
			
		}
	}
//---------------------------------------------------------------------------------------------------------------------------
@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// TODO Auto-generated method stub
	super.onCreateOptionsMenu(menu,inflater);
	inflater.inflate(R.menu.main, menu);
}	
//=========================== get all ===========================
  class GetAll extends AsyncTask<Void, Void, Void>{
	  int i=0;
	  private ProgressDialog progressDialog;
	private StringBuilder stringBuilder;
	private String res;
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		  progressDialog = DialogFactory
					 .showOnlySpinningWheel(getActivity());
		super.onPreExecute();
	}
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub

		
		String id_res = preferences.getString("id", "");
		Log.d("==id_res","=====--"+id_res);
		HttpClient httpclient = new DefaultHttpClient();
		// phone_str=phone1.getText().toString().trim();
		HttpGet httpGet = new HttpGet(ConstantUrlClass.Get_All);
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
		a=0;
		b=1;
		try {
			
			JSONObject obj = new JSONObject(res);
			status = obj.getString("status");
			if (status.equals("true")) {
				final JSONArray data = obj.getJSONArray("data");
				if(data.length()>1){
				
				
					
					startTimer1(data);
					
					
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
  }
  public void initializeTimerTask1(final JSONArray data) {
	 
	  
	  handler.post(new Runnable(){
	        private double km_get;

			@Override
	        public void run() {
	        	try {
	        		
//	        		 BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.track_icon_school);
//	                 GroundOverlayOptions groundOverlay = new GroundOverlayOptions()
//	                 .image(image)
//	                 .position(
//	        					new LatLng(Double.valueOf(end_latitude.toString()), Double
//	        							.valueOf(end_longitude.toString())), 500f)
//	                 ;
//	        		
//	                 map.addGroundOverlay(groundOverlay);
	        		
	        		
	        		
	        		
	        		
					Polyline line = map
							.addPolyline(new PolylineOptions()
									.add(new LatLng(
											Double.parseDouble(data
													.getJSONObject(a)
													.getString("latitude").toString()),
											Double.parseDouble(data
													.getJSONObject(a)
													.getString("longitude").toString())),
											new LatLng(
													Double.parseDouble(data
															.getJSONObject(b)
															.getString(
																	"latitude").toString()),
													Double.parseDouble(data
															.getJSONObject(b)
															.getString("longitude").toString())))
									.width(8).color(Color.BLUE).geodesic(true)
									.visible(true));
					
					//======================= add the cuurent image icon in map======================
					
					if((current_location1==null))
					{
						BitmapDescriptor i2 = BitmapDescriptorFactory
								.fromResource(R.drawable.track);						
//						  LatLng mapCenter = new LatLng(Double.parseDouble(data
//									.getJSONObject(b)
//									.getString(
//											"latitude").toString()), Double.parseDouble(data
//													.getJSONObject(b)
//													.getString(
//															"longitude").toString()));
//
//						 
//						  
//						  
//						  current_location = map.addMarker(new MarkerOptions()
//								.position(
//										new LatLng(Double.parseDouble(data
//												.getJSONObject(b)
//												.getString(
//														"latitude").toString()), Double.parseDouble(data
//																.getJSONObject(b)
//																.getString(
//																		"longitude").toString()))).icon(i2).flat(true).position(mapCenter)
//								.title("Current location"));
						  
						  
						  BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.track);
			                 GroundOverlayOptions groundOverlay = new GroundOverlayOptions()
			                 .image(image)
			                 .position(
										new LatLng(Double.parseDouble(data
												.getJSONObject(b)
												.getString(
														"latitude").toString()), Double.parseDouble(data
																.getJSONObject(b)
																.getString(
																		"longitude").toString())), 150f)
			                 ;
			        		
			                 current_location1 =  map.addGroundOverlay(groundOverlay);
						  
						
					}
					if(current_location1!=null)
					{
						current_location1.remove();
//						BitmapDescriptor i2 = BitmapDescriptorFactory
//								.fromResource(R.drawable.track);
						
		        		 BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.track);
		                 GroundOverlayOptions groundOverlay = new GroundOverlayOptions()
		                 .image(image)
		                 .position(
									new LatLng(Double.parseDouble(data
											.getJSONObject(b)
											.getString(
													"latitude").toString()), Double.parseDouble(data
															.getJSONObject(b)
															.getString(
																	"longitude").toString())), 100f)
		                 ;
		        		
		                 current_location1 = map.addGroundOverlay(groundOverlay);
						
						
//						  LatLng mapCenter = new LatLng(Double.parseDouble(data
//									.getJSONObject(b)
//									.getString(
//											"latitude").toString()), Double.parseDouble(data
//													.getJSONObject(b)
//													.getString(
//															"longitude").toString()));
//
//						current_location = map.addMarker(new MarkerOptions()
//								.position(
//										new LatLng(Double.parseDouble(data
//												.getJSONObject(b)
//												.getString(
//														"latitude").toString()), Double.parseDouble(data
//																.getJSONObject(b)
//																.getString(
//																		"longitude").toString()))).icon(i2).flat(true).position(mapCenter)
//								.title("Current location"));
					}
					LocationAddress locationAddress = new LocationAddress(2);
			        locationAddress.getAddressFromLocation(Double.parseDouble(data
							.getJSONObject(b)
							.getString(
									"latitude").toString()), Double.parseDouble(data
											.getJSONObject(b)
											.getString(
													"longitude").toString()),
			                getActivity(), new GeocoderHandler());
			        
			        Double third_lat = Double.parseDouble(data
							.getJSONObject(2)
							.getString("latitude").toString());
					Double third_lang = Double.parseDouble(data
							.getJSONObject(2)
							.getString("longitude").toString());
					//start
					 end_latitude = preferences.getString("end_latitude", "");
					 end_longitude = preferences.getString("end_longitude", "");
					 Double end_latitude_a = Double.parseDouble(end_latitude.toString());
					 Double end_longitude_b = Double.parseDouble(end_longitude.toString());
					 km_first =getDistanceFromLatLonInKm(third_lat,third_lang,end_latitude_a,end_longitude_b);
					
					//end
					user_latitude = preferences.getString("user_latitude", "");
					user_longitude = preferences.getString("user_longitude", "");						 
					Double user_latitude_a = Double.parseDouble(user_latitude.toString());
					Double user_longitude_b = Double.parseDouble(user_longitude.toString());						
					km_Second =getDistanceFromLatLonInKm(third_lat,third_lang,user_latitude_a,user_longitude_b);
					
					Log.d("==km_first","==="+km_first);
					Log.d("==km_Second","==="+km_Second);
					
					if(km_first<km_Second){
						Log.d("==km_Second","==="+"School to Home");
						hometoany.setText("School to Home ");
						which_way="home";
						//km_get=km_Second;
						Double last_lat = Double.parseDouble(data
								.getJSONObject(b)
								.getString("latitude").toString());
						Double last_long = Double.parseDouble(data
								.getJSONObject(b)
								.getString("longitude").toString());
						km_get =getDistanceFromLatLonInKm(last_lat,last_long,user_latitude_a,user_longitude_b);
						Log.d("==","==--=="+"end====="+km_get);

					}else{	
						Log.d("==km_Second","==="+"Home to school");

						hometoany.setText("Home to school");
						which_way="school";
						//km_get=km_first;
						Double last_lat = Double.parseDouble(data
								.getJSONObject(b)
								.getString("latitude").toString());
						Double last_long = Double.parseDouble(data
								.getJSONObject(b)
								.getString("longitude").toString());
						km_get =getDistanceFromLatLonInKm(last_lat,last_long,end_latitude_a,end_longitude_b);
						Log.d("==","==--=="+"start====="+km_get);
					}
					String upToNCharacters="0";
					float time=(float) ((km_get/30)*60);
					StringTokenizer tokens = new StringTokenizer(String.valueOf(time), ".");
					while (tokens.hasMoreTokens()) {
						
					
					String firststring = tokens.nextToken();
					
					String secondstring = tokens.nextToken();
					Log.d("==secondstring web","=--=="+secondstring);
					if(secondstring.equals("")||secondstring==null||secondstring.isEmpty()){
					}else{
							if(secondstring.length()==1){
								
							}else{
							upToNCharacters = secondstring.substring(0, 2);
							}
						
						if(Integer.parseInt(upToNCharacters)>60){
							upToNCharacters="60";
						}
						distances.setText(firststring+" min "+String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(upToNCharacters.toString())))+" sec "+"("+String.valueOf(new DecimalFormat("##.##").format(km_get))+" km)");
						Log.d("==kmrange","=="+km_first);
					}
					}
					
					if(km_get <=0.100000){
						//stoptimertask();
					//	a=0;b=1;
						
						//new ReachHome().execute();
						
						
						hometoany.setText("");
						if(km_first<km_Second){
							hometoany.setText("Student Reached Home");
						}else{						
							hometoany.setText("Student Reached School");
						}
						//progressDialog.dismiss();
					}else{
						//progressDialog.dismiss();
						// draw path for eg. 1 to 10 cordinate and after 10 to home draw using timer
						//startTimer();
					}
					
					a++;b++;
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace(); 
				} 
	        	//i++;
	            
	        	if(b < data.length()-1) {
	            
	        		handler.postDelayed(this, 500);
	            
	        	}
	        }
	    });
	  
	  
  }
	public void startTimer1(final JSONArray data) {		
        //set a new Timer
        timer = new Timer();

        initializeTimerTask1(data);
  
        timer.schedule(timerTask, 500, 3000); //

    }
}
