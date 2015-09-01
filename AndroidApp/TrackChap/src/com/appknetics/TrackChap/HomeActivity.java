package com.appknetics.TrackChap;

import java.util.ArrayList;
import java.util.List;

import com.appknetics.TrackChap.R;
import com.fragments.Constantclass;
import com.fragments.EditProfile;
import com.fragments.GoogleMap_Frag;
import com.fragments.Notification;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;




@SuppressLint("NewApi")
public class HomeActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	ImageButton imageButton;
	RelativeLayout relativelayout;
	private String userId,token;
	SharedPreferences preferences2;
	SharedPreferences.Editor editor2;
	private final static String TAG_FRAGMENT = "TAG_FRAGMENT";
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	String id,wallker_id;
	private ActionBar mActionBar;
	TextView address;
	private LinearLayout linearprofileimage;
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 wallker_id=getIntent().getStringExtra("id");
		
		mTitle = mDrawerTitle = getTitle();
		Log.d("", "mTitle==" + mTitle);
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		relativelayout=(RelativeLayout)findViewById(R.id.relativelayout);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		int n =navMenuTitles.length;
		for (int i = 0; i < n; i++) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1)));
		}
		mActionBar = getActionBar();
		int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
		TextView abTitle = (TextView) findViewById(titleId);
		abTitle.setTextColor(Color.parseColor("#000000"));
		mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
		
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.actionbar_layout, null);
		mActionBar.setCustomView(mCustomView);
		
		Typeface contact_us_font = Typeface.createFromAsset(getAssets(), "Gotham-Bold.otf");
		
		 address=(TextView)mCustomView.findViewById(R.id.address);
		address.setTypeface(contact_us_font);
		
		
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		
		// Recycle the typed array
		navMenuIcons.recycle();

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		/*
		 * ========Add image on Action action Bar========== LayoutInflater
		 * mInflater = LayoutInflater.from(this);
		 * 
		 * View mCustomView = mInflater.inflate(R.layout.imageview, null);
		 * imageButton = (ImageButton) mCustomView
		 * .findViewById(R.id.imageView4); imageButton.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View view) {
		 * Toast.makeText(getApplicationContext(), "Refresh Clicked!",
		 * Toast.LENGTH_LONG).show(); } });
		 * 
		 * getActionBar().setCustomView(mCustomView);
		 * getActionBar().setDisplayShowCustomEnabled(true);
		 * //==========end=====================================
		 */
		// change atctionbar color
//		getActionBar().setBackgroundDrawable(
//				new ColorDrawable(Color.parseColor("#a7a7a7")));
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.drawer, R.string.app_name,R.string.app_name) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// imageButton.setVisibility(View.VISIBLE);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// imageButton.setVisibility(View.INVISIBLE);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			for( int i=0; i<parent.getChildCount(); i++)
            {
                 if(i == position)
                 {
                	 parent.getChildAt(i).setBackgroundColor(Color.parseColor("#0ac1f0"));
                	 
                 }
                 else
                 {
                	 parent.getChildAt(i).setBackgroundColor(Color.parseColor("#303539"));
                 }
             }	
			displayView(position);
			
		}

	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	@SuppressWarnings("unused")
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			
			if (new ConnectionDetector(HomeActivity.this).isConnectingToInternet()) {
				FragmentManager manager =getSupportFragmentManager();			
				FragmentTransaction transaction=manager.beginTransaction();

				GoogleMap_Frag screen=new GoogleMap_Frag();
				transaction.replace(R.id.frame_container,screen);
				transaction.commit();			
				
				
				address.setText("BusTracker");
			} else {
				Toast.makeText(this, "No internet connectivity.",
						Toast.LENGTH_SHORT).show();
			}
			mDrawerLayout.closeDrawers();	
			
			break;
//		case 1:
//			if (new ConnectionDetector(HomeActivity.this).isConnectingToInternet()) {
//				FragmentManager manager1 =getSupportFragmentManager();			
//				FragmentTransaction transaction1=manager1.beginTransaction();
//				EditProfile screen1=new EditProfile();
//				transaction1.replace(R.id.frame_container,screen1);
//				transaction1.commit();			
//				address.setText("Edit Location");
//				
//				
//			} else {
//				Toast.makeText(this, "No internet connectivity.",
//						Toast.LENGTH_SHORT).show();
//			}
//			mDrawerLayout.closeDrawers();
//			
//			
//			break;
		case 1:
			address.setText("Notification");

			FragmentManager manager2 =getSupportFragmentManager();			
			FragmentTransaction transaction2=manager2.beginTransaction();
			Notification screen2=new Notification();
			transaction2.replace(R.id.frame_container,screen2);
			transaction2.commit();	
			mDrawerLayout.closeDrawers();
			break;
//		case 3:
//			address.setText("Attendences");
//
//			FragmentManager manager31 =getSupportFragmentManager();			
//			FragmentTransaction transaction31=manager31.beginTransaction();
//			Attendences screen31=new Attendences();
//			transaction31.replace(R.id.frame_container,screen31);
//			transaction31.commit();	
//			mDrawerLayout.closeDrawers();
//			break;
		case 2:
			
			address.setText("Contact Us");
			int flag=2;
			FragmentManager contact_us =getSupportFragmentManager();			
			FragmentTransaction transaction3=contact_us.beginTransaction();
			Contact_us screen3=new Contact_us(flag);
			transaction3.replace(R.id.frame_container,screen3);
			transaction3.commit();	
			mDrawerLayout.closeDrawers();

			break;
		case 3:
			address.setText("Take A Tour");
			
			FragmentManager tuto =getSupportFragmentManager();			
			FragmentTransaction transaction4=tuto.beginTransaction();
			Tutorial_Fragment screen4=new Tutorial_Fragment();
			transaction4.replace(R.id.frame_container,screen4);
			transaction4.commit();	
			mDrawerLayout.closeDrawers();
		
			// fragment = new NutritiionFragment();
			break;
		case 4:
			address.setText("Logout");

			preferences = getSharedPreferences("tracker",
					MODE_PRIVATE);
			editor = preferences.edit();
			editor.remove("configration_flag");
			editor.clear();
			editor.commit();
			
			Intent i=new Intent(HomeActivity.this,Login_Activity.class);
			startActivity(i);
			finish();
			
			mDrawerLayout.closeDrawers();
		
			// fragment = new NutritiionFragment();
			break;
		

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
			

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(relativelayout);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.login, menu);
	//	 getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		  switch (item.getItemId()) {
	        case R.id.title_menu:
	            Log.v( "User2","==--"+ "title" );

	        	FragmentManager manager =getSupportFragmentManager();			
				FragmentTransaction transaction=manager.beginTransaction();
				GoogleMap_Frag screen=new GoogleMap_Frag();
				transaction.replace(R.id.frame_container,screen);
				transaction.commit();			
				
				
				//address.setText("Student Tracker");
	        	
	            return true;
	        default:
	        	return super.onOptionsItemSelected( item );
	    }
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(relativelayout);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	preferences = getSharedPreferences("tracker",
			MODE_PRIVATE);
	editor = preferences.edit();
	editor.remove("inside_app");
	editor.commit();
	super.onDestroy();
	
}

@Override
protected void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	preferences = getSharedPreferences("tracker",
			MODE_PRIVATE);
	editor = preferences.edit();
	editor.remove("inside_app");
	editor.commit();
}

}
