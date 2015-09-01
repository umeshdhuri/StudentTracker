package com.appknetics.TrackChap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;




import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Attendences extends Fragment {

	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	ArrayList<String> event;
	LinearLayout rLayout;
	ArrayList<String> date;
	ArrayList<String> desc;
	TextView title;
	Random color1,color2,color3,color4,color5;
	GridView gridview;
	ImageView Present_img,absent_img,hoilday_img,weekly_hoilday_img,notapplicable_img;
	public Attendences() {
		// Required empty public constructor
	}

	 @SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v= inflater
				.inflate(R.layout.fragment_attendences, container, false);
		color1=new Random();
		color2=new Random();
		color3=new Random();
		color4=new Random();
		color5=new Random();
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Book.otf");

		TextView sun=(TextView)v.findViewById(R.id.sun);
		TextView mon=(TextView)v.findViewById(R.id.mon);
		TextView tue=(TextView)v.findViewById(R.id.tue);
		TextView wed=(TextView)v.findViewById(R.id.wed);
		TextView thu=(TextView)v.findViewById(R.id.thu);
		TextView fri=(TextView)v.findViewById(R.id.fri);
		TextView sat=(TextView)v.findViewById(R.id.sat);

		sun.setTypeface(font);
		mon.setTypeface(font);
		tue.setTypeface(font);
		wed.setTypeface(font);
		thu.setTypeface(font);
		fri.setTypeface(font);
		sat.setTypeface(font);

		TextView present=(TextView)v.findViewById(R.id.present);
		TextView absent=(TextView)v.findViewById(R.id.absent);
		TextView hoilday=(TextView)v.findViewById(R.id.hoilday);
		TextView weekly_holiday=(TextView)v.findViewById(R.id.weekly_holiday);
		TextView notapp=(TextView)v.findViewById(R.id.notapp);
		present.setTypeface(font);
		absent.setTypeface(font);
		hoilday.setTypeface(font);
		weekly_holiday.setTypeface(font);
		notapp.setTypeface(font);
		rLayout = (LinearLayout) v.findViewById(R.id.text);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();
		 title = (TextView)v.findViewById(R.id.title);
			title.setTypeface(font);
			
		 Present_img=(ImageView)v.findViewById(R.id.Present_img);
		 absent_img=(ImageView)v.findViewById(R.id.absent_img);
		 hoilday_img=(ImageView)v.findViewById(R.id.hoilday_img);
		 weekly_hoilday_img=(ImageView)v.findViewById(R.id.weekly_hoilday_img);
		 notapplicable_img=(ImageView)v.findViewById(R.id.notapplicable_img);

		  GradientDrawable shape1 =  new GradientDrawable();
		    shape1.setCornerRadius(130);
		    shape1.setColor(Color.parseColor("#6d97f9"));		
		    if (Build.VERSION.SDK_INT >= 16) {
		    	Present_img.setBackground(shape1);
		    } else {
		    	Present_img.setBackgroundDrawable(shape1);
		    }
		   // Present_img.setBackground(shape1);
		 
		    GradientDrawable shape2 =  new GradientDrawable();
		    shape2.setCornerRadius(130);
		    shape2.setColor(Color.parseColor("#ff0000"));				   
		    if (Build.VERSION.SDK_INT >= 16) {
		    	absent_img.setBackground(shape2);
		    } else {
		    	absent_img.setBackgroundDrawable(shape2);
		    }
		   // absent_img.setBackground(shape2);
		    
		    GradientDrawable shape3 =  new GradientDrawable();
		    shape3.setCornerRadius(130);
		    shape3.setColor(Color.parseColor("#ffa600"));				   
		    if (Build.VERSION.SDK_INT >= 16) {
		    	hoilday_img.setBackground(shape3);
		    } else {
		    	hoilday_img.setBackgroundDrawable(shape3);
		    }
		   // hoilday_img.setBackground(shape3);
		    
		    GradientDrawable shape4 =  new GradientDrawable();
		    shape4.setCornerRadius(130);
		    shape4.setColor(Color.parseColor("#800080"));				   
		    if (Build.VERSION.SDK_INT >= 16) {
		    	weekly_hoilday_img.setBackground(shape4);
		    } else {
		    	weekly_hoilday_img.setBackgroundDrawable(shape4);
		    }
		   // weekly_hoilday_img.setBackground(shape4);
		    
		    GradientDrawable shape5 =  new GradientDrawable();
		    shape5.setCornerRadius(130);
		    shape5.setColor(Color.parseColor("#00ff00"));				   
		  //  notapplicable_img.setBackground(shape5);
		    if (Build.VERSION.SDK_INT >= 16) {
		    	notapplicable_img.setBackground(shape5);
		    } else {
		    	notapplicable_img.setBackgroundDrawable(shape5);
		    }
		items = new ArrayList<String>();

		adapter = new CalendarAdapter(getActivity(), month);

		 gridview = (GridView) v.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		handler = new Handler();
		handler.post(calendarUpdater);

		TextView title = (TextView)v. findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = (RelativeLayout)v. findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				refreshCalendar();
			}
		});

		RelativeLayout next = (RelativeLayout)v. findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				
			}

		});
		return v;
	}
	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),
					month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);
		}

	}

	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}

	}

	protected void showToast(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

	}

	public void refreshCalendar() {

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();

			// Print dates of the current week
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			String itemvalue;
			event = Utility.readCalendarEvent(getActivity());
			Log.d("=====Event====", event.toString());
			Log.d("=====Date ARRAY====", Utility.startDates.toString());

			for (int i = 0; i < Utility.startDates.size(); i++) {
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(GregorianCalendar.DATE, 1);
				items.add(Utility.startDates.get(i).toString());
			}
			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};
}
