package com.fragments;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


import com.appknetics.TrackChap.Notification_adapter;
import com.appknetics.TrackChap.R;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class Notification extends Fragment implements Delete_interface
{
	ArrayList<Noti_uti> notilist;
	ListView notification_list;
	private Cursor cursor;
	private DBHelper dbHelper;
	private Notification_adapter adapter;
	Delete_interface touch;
	TextView No_noti;
	public Notification(){
		
	}	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		dbHelper = DBHelper.getInstance(getActivity());
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		final TextView view = (TextView) getActivity().findViewById(R.id.clear);
		if(notilist.isEmpty()){
			view.setVisibility(View.GONE);
		}else{
		
		view.setVisibility(View.VISIBLE);
		}
		view.setTextColor(Color.parseColor("#a7a7a7"));
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(getActivity())
			    .setMessage("Are you sure you want to delete this All notification.")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // continue with delete
			        	dbHelper.delete("notification", null,null);
						
						Toast.makeText(getActivity(), "Clear all Data",Toast.LENGTH_SHORT).show();
				    	notilist.clear();
				    	notification_list.setAdapter(adapter);
			        }
			     })
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
				
				
			}
		});
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Constantclass.home_flag="0";
		
		try {
			get_q_list();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Typeface contact_us_font = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Light.otf");

		View v = inflater.inflate(R.layout.notification, container, false);
		No_noti=(TextView)v.findViewById(R.id.No_noti);
		No_noti.setTypeface(contact_us_font);
		notification_list=(ListView)v.findViewById(R.id.notification_list);
		 adapter = new Notification_adapter(getActivity(), notilist,Notification.this);
		
		if(notilist.isEmpty()){
			No_noti.setVisibility(View.VISIBLE);
			notification_list.setVisibility(View.GONE);
		}else{
			No_noti.setVisibility(View.GONE);
			notification_list.setVisibility(View.VISIBLE);
		}
		notification_list.setAdapter(adapter);

		return v;
	}
	
	@Override
	public void delete(String url) {
		dbHelper.delete("notification", "posted_on" + "=?",new String[]{url.toString()});
		
		Toast.makeText(getActivity(), "delete From Database",Toast.LENGTH_SHORT).show();
		
		for(int i=0;i<notilist.size();i++)
		{
		    if(notilist.get(i).getDuration().equals(url))
		    {
		    	notilist.remove(i);
		    }
		}
    	notification_list.setAdapter(adapter);
	}
	
	public void get_q_list() throws IOException {
		Cursor cursor = dbHelper.select("notification", null, null, null, null,
				null, null);
		if((cursor==null)||(cursor.equals(""))){
			
		}else{
		List<Noti_uti> noti = new ArrayList<Noti_uti>();
		notilist=new ArrayList<Noti_uti>();

		if (cursor.moveToFirst()) {
			do {
				noti.add(new Noti_uti((cursor.getString(cursor
						.getColumnIndex("uid"))), (cursor.getString(cursor
						.getColumnIndex("title"))), (cursor.getString(cursor
						.getColumnIndex("posted_on"))))
						);
				
				notilist.add(new Noti_uti((cursor.getString(cursor
						.getColumnIndex("uid"))), (cursor.getString(cursor
						.getColumnIndex("title"))), (cursor.getString(cursor
						.getColumnIndex("posted_on"))))
						);
			} while (cursor.moveToNext());
		}
	}
		 if (cursor != null) {
		 cursor.close();
		 cursor = null;
		 }
	}
	@Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
    	inflater.inflate(R.menu.main, menu);
	}
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
	   // menu.findItem(R.id.title_menu).setVisible(false).setEnabled(false);	
		Log.d("==onPrepareOptionsMenu","==----"+"onPrepareOptionsMenu");
	    menu.findItem(R.id.title_menu).setVisible(false).setEnabled(false);
	}
}
