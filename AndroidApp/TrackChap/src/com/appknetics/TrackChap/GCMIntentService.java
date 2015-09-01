package com.appknetics.TrackChap;

import static com.appknetics.TrackChap.CommonUtilities.SENDER_ID;
import static com.appknetics.TrackChap.CommonUtilities.displayMessage;

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
import com.appknetics.TrackChap.Login_Activity.Confirm_authentication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fragments.DBHelper;
import com.google.android.gcm.GCMBaseIntentService;

@SuppressLint("NewApi") public class GCMIntentService extends GCMBaseIntentService {
String message;
//	private NeedyDBHelper dbHelper;
//	public NeedyBean bean;
	private String notificationFlag, reminderFlag;
	private SharedPreferences pref;
	private SQLiteDatabase db;
	private Cursor notificationCursor;
	private DBHelper dbHelper;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	private char[] message1;
	public GCMIntentService() {
		super(SENDER_ID);
		//bean = new NeedyBean();
		
		
		}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {

		displayMessage(context, "Registred with GCM");

		Log.d("reg", "regis id GCM == " + registrationId);

	}

	/**
	 * Method called on device un registred
	 * */

	protected void onUnregistered(Context context, String registrationId) {

		displayMessage(context,"From GCM: device successfully unregistered!");
	}

	/**
	 * Method called on Receiving a new message
	 * */
	protected void onMessage(Context context, Intent intent) {
		dbHelper = DBHelper.getInstance(context);
		preferences=getSharedPreferences("tracker",MODE_PRIVATE);
		editor=preferences.edit();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new Configuration(context,intent).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			new Configuration(context,intent).execute();
		}
	//	new Configuration().execute();
		
		
		
	}
	
	/**
	 * Method called on receiving a deleted message
	 * */

	protected void onDeletedMessages(Context context, int total) {
		String message = "abc";
		//getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * com.androidhive.pushnotifications Method called on Error
	 * */

	public void onError(Context context, String errorId) {
		displayMessage(context,"abc");
		//getString(R.string.gcm_error, errorId));
	}

	protected boolean onRecoverableError(Context context, String errorId) {
//		displayMessage(context,
//				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressWarnings({ "deprecation", "null" })
	private void generateNotification(Context context, String message) {
		//int icon = R.drawable.;
		int icon = this.getResources().getIdentifier("track_icon_home", "drawable", this.getPackageName());
		long when = System.currentTimeMillis();
		
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title =message;
		Intent notificationIntent = null;
		
//		notificationIntent = new Intent(GCMIntentService.this,
//				HomeActivity.class);
//		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		//notificationIntent.putExtra("tabNumber", "2");
//		 startActivity(notificationIntent);
		
//		pref = context
//				.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
//		if (pref.getInt("userId", 0) == 0) {
////			notificationIntent = new Intent(context,
////					NeedyWelcomeActiviity.class);
//		} else {
//			try {
//				// NeedyBean.setNotificationCheck(true);
////				notificationIntent = new Intent(GCMIntentService.this,
////						MainTabActivity.class);
////				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////				notificationIntent.putExtra("tabNumber", "2");
//				// startActivity(notificationIntent);
//				// NeedyBean.setIsmyTask(false);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		 
			String	inside_app = preferences.getString("inside_app", "");
			if(inside_app.equals("inside")){
				// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				 notificationIntent = new Intent(GCMIntentService.this,
//							SplashScreenActivity.class);
				 
				PendingIntent intent = PendingIntent.getActivity(getApplicationContext(),0,new Intent(),// add this
					    PendingIntent.FLAG_UPDATE_CURRENT);
				notification.setLatestEventInfo(context, title, message, intent);

			}else{
				notificationIntent = new Intent(GCMIntentService.this,
						HomeActivity.class);
				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//notificationIntent.putExtra("tabNumber", "2");
				 startActivity(notificationIntent);
				 PendingIntent intent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
					notification.setLatestEventInfo(context, title, message, intent);
			}
			
		 
		// set intent so it does not start a new activity
		

		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		try {
			notificationFlag="1";
			if (notificationFlag.equals("1")) {

				
					// Play default notification sound
					//notification.defaults |= Notification.DEFAULT_SOUND;
					// notification.sound = Uri.parse("android.resource://"
					// + context.getPackageName() + "/raw/notification2");
					// Vibrate if vibrate is enabled
					notification.defaults |= Notification.DEFAULT_VIBRATE;

				
			
					// Vibrate if vibrate is enabled
					notification.defaults |= Notification.DEFAULT_VIBRATE;

			
					// Play default notification sound
					//notification.defaults |= Notification.DEFAULT_SOUND;
					// notification.sound = Uri.parse("android.resource://"
					// + context.getPackageName() + "/raw/notification2");

				
			}
			reminderFlag="1";
			if (reminderFlag.equals("1")) {
				// Play default notification sound
			//	notification.defaults |= Notification.DEFAULT_LIGHTS;
//				Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//				notification.sound = uri;
//				notification.sound = Uri.parse("android.resource://"
//						+ context.getPackageName() + "/raw/schoolbell");
				notification.sound = Uri.parse("android.resource://com.appknetics.TrackChap/raw/_choolbell");
				// Vibrate if vibrate is enabled
				notification.defaults |= Notification.DEFAULT_VIBRATE;

			}
			notificationManager.notify(0, notification);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		Context context;
		Intent intent;
		public Configuration(Context context, Intent intent) {
			// TODO Auto-generated constructor stub
			this.context=context;
			this.intent=intent;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			progressDialog = DialogFactory
//					.showOnlySpinningWheel(GCMIntentService.this);
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

					
				//	progressDialog.dismiss();
//					Intent intent=new Intent(GCMIntentService.this,HomeActivity.class);
//					startActivity(intent);
//					finish();
				//	Toast.makeText(GCMIntentService.this, "Login Sucessfull",Toast.LENGTH_SHORT).show();
				}else{
					//Toast.makeText(GCMIntentService.this, "Invalid mobile number",Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
			}
			try {
				// message1 = intent.getExtras().getCharArray("aps");
				 message = intent.getExtras().getString("alert");
				String user_id = intent.getExtras().getString("uid");
				String alert = intent.getExtras().getString("posted_on");
				
				Log.d("device_id", "=--=" + alert);
				Log.d("user_id", "=--=" + user_id);
				Log.d("message", "=--=" + message);
				
			
				ContentValues contentValues = new ContentValues();
				contentValues.put("uid", user_id);
				contentValues.put("title",  message);
				contentValues.put("posted_on",alert);
				dbHelper.insert("notification", contentValues);
				
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("Innnn", "==" + "In exception") ;
			}
			
//				// notificationMessage.notificationList();
				displayMessage(context, message);
//				// notifies user
				generateNotification(context, message);
			
			
		}
	}
	
}
