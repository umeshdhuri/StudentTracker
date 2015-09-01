/*
 * Copyright (C) 2014 Mukesh Y authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appknetics.TrackChap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Mukesh Y
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN) public class CalendarAdapter extends BaseAdapter {
	private Context mContext;

	private java.util.Calendar month;
	public GregorianCalendar pmonth; // calendar instance for previous month
	/**
	 * calendar instance for previous month for getting complete view
	 */
	public GregorianCalendar pmonthmaxset;
	private GregorianCalendar selectedDate;
	int firstDay;
	int maxWeeknumber;
	int maxP;
	int calMaxP;
	int lastWeekDay;
	int leftDays;
	int mnthlength;
	String itemvalue, curentDateString;
	DateFormat df;

	private ArrayList<String> items;
	public static List<String> dayString;
	private View previousView;

	private int flag;
	TextView dayView,circle;
	Calendar cc;
	int month11;
	int ii=0,day,year;
	public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
		CalendarAdapter.dayString = new ArrayList<String>();
		Locale.setDefault(Locale.US);
		month = monthCalendar;
		selectedDate = (GregorianCalendar) monthCalendar.clone();
		mContext = c;
		month.set(GregorianCalendar.DAY_OF_MONTH, 1);
		this.items = new ArrayList<String>();
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		curentDateString = df.format(selectedDate.getTime());
		refreshDays();
		Calendar cc = Calendar.getInstance();
		month11 = cc.get(Calendar.MONTH);
		year = cc.get(Calendar.YEAR);

		 day = cc.get(Calendar.DAY_OF_MONTH);

	}

	public void setItems(ArrayList<String> items) {
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				items.set(i, "0" + items.get(i));
			}
		}
		this.items = items;
	}

	public int getCount() {
		return dayString.size();
	}

	public Object getItem(int position) {
		return dayString.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new view for each item referenced by the Adapter
	 @TargetApi(Build.VERSION_CODES.JELLY_BEAN) public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.calendar_item, null);

		}
		dayView = (TextView) v.findViewById(R.id.date);
		Typeface font = Typeface.createFromAsset(mContext.getAssets(), "Gotham-Book.otf");
		dayView.setTypeface(font);
		circle= (TextView) v.findViewById(R.id.circle);
		
		// separates daystring into parts.
		String[] separatedTime = dayString.get(position).split("-");
		// taking last part of date. ie; 2 from 2012-12-02
		String gridvalue = separatedTime[2].replaceFirst("^0*", "");
		String MONTH = separatedTime[1].replaceFirst("^0*", "");

		Log.d("==gridvalue","=--="+MONTH);
		if (dayString.get(position).equals(curentDateString)) {
			setSelected(v,position);
			previousView = v;
			
		} else {
			v.setBackgroundResource(R.drawable.list_item_background);
		}
		// checking whether the day is in current month or not.
		if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
			// setting offdays to white color.
			dayView.setTextColor(Color.WHITE);
			dayView.setClickable(false);
			dayView.setFocusable(false);
			circle.setVisibility(View.GONE);

		} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
			dayView.setTextColor(Color.WHITE);
			dayView.setClickable(false);
			dayView.setFocusable(false);
			circle.setVisibility(View.GONE);

		}	
		else {
			
			
			
			
			Log.d("==INSIDE MONTH","=--="+month);
			Log.d("==year indise","=----="+(month.get(GregorianCalendar.YEAR)));
			Log.d("==year","=----="+year);

			
			if(day>=Integer.parseInt(gridvalue)&& ((month11+1)>=Integer.parseInt(MONTH))&&(month.get(GregorianCalendar.YEAR))==year)
			{
			if(String.valueOf(month11+1).equals(String.valueOf(MONTH))){		
			
			
			// setting curent month's days in blue color.
			if(((position)%7==0 )||((position)%7==6 ))
			{

				if(position<=flag){
					ii=1;
					circle.setVisibility(View.VISIBLE);
					GradientDrawable shape4 =  new GradientDrawable();
				    shape4.setCornerRadius(130);
				    shape4.setColor(Color.parseColor("#800080"));				   
				    if (Build.VERSION.SDK_INT >= 16) {
				    	circle.setBackground(shape4);
				    } else {
				    	circle.setBackgroundDrawable(shape4);
				    }
				   // circle.setBackground(shape4);
				
				}
			}else{	
				
				if(position<=flag){
					ii=1;
					circle.setVisibility(View.VISIBLE);
					GradientDrawable shape1 =  new GradientDrawable();
				    shape1.setCornerRadius(130);
				    shape1.setColor(Color.parseColor("#6d97f9"));				   
				    if (Build.VERSION.SDK_INT >= 16) {
				    	circle.setBackground(shape1);
				    } else {
				    	circle.setBackgroundDrawable(shape1);
				    }
				   // circle.setBackground(shape1);				   
				}else{
					dayView.setTextColor(Color.BLUE);
				}
			}
		}
		}
		
			
			else{
			circle.setVisibility(View.GONE);
			dayView.setTextColor(Color.BLUE);

		}
			dayView.setTextColor(Color.BLUE);
			if((month.get(GregorianCalendar.YEAR))==year)
			{
			if(String.valueOf(month11+1).equals(String.valueOf(MONTH))){		
				
				
				// setting curent month's days in blue color.
				if(((position)%7==0 )||((position)%7==6 ))
				{

					if(position<=flag){
						ii=1;
						circle.setVisibility(View.VISIBLE);
						GradientDrawable shape4 =  new GradientDrawable();
					    shape4.setCornerRadius(130);
					    shape4.setColor(Color.parseColor("#800080"));				   
					    if (Build.VERSION.SDK_INT >= 16) {
					    	circle.setBackground(shape4);
					    } else {
					    	circle.setBackgroundDrawable(shape4);
					    }
					   // circle.setBackground(shape4);
					
					}
				}else{	
					
					if(position<=flag){
						ii=1;
						circle.setVisibility(View.VISIBLE);
						GradientDrawable shape1 =  new GradientDrawable();
					    shape1.setCornerRadius(130);
					    shape1.setColor(Color.parseColor("#6d97f9"));				   
					    if (Build.VERSION.SDK_INT >= 16) {
					    	circle.setBackground(shape1);
					    } else {
					    	circle.setBackgroundDrawable(shape1);
					    }
					   // circle.setBackground(shape1);				   
					}else{
						dayView.setTextColor(Color.BLUE);
					}
				}
			}else 
			if((month11+1)>=Integer.parseInt(MONTH)){
					
					if(((position)%7==0 )||((position)%7==6 ))
					{

							
							circle.setVisibility(View.VISIBLE);
							GradientDrawable shape4 =  new GradientDrawable();
						    shape4.setCornerRadius(130);
						    shape4.setColor(Color.parseColor("#800080"));				   
						    if (Build.VERSION.SDK_INT >= 16) {
						    	circle.setBackground(shape4);
						    } else {
						    	circle.setBackgroundDrawable(shape4);
						    }
						   // circle.setBackground(shape4);
						
						
					}else {
						circle.setVisibility(View.VISIBLE);
						GradientDrawable shape1 =  new GradientDrawable();
					    shape1.setCornerRadius(130);
					    shape1.setColor(Color.parseColor("#6d97f9"));				   
					    if (Build.VERSION.SDK_INT >= 16) {
					    	circle.setBackground(shape1);
					    } else {
					    	circle.setBackgroundDrawable(shape1);
					    }
					}
					
					
				}
		}else{
			circle.setVisibility(View.GONE);
			dayView.setTextColor(Color.BLUE);

		}
		
		
		}
			
		
		dayView.setText(gridvalue);
		
		// create date string for comparison
		String date = dayString.get(position);

		if (date.length() == 1) {
			date = "0" + date;
		}
		String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}

		// show icon if date is not empty and it exists in the items array
		ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
		if (date.length() > 0 && items != null && items.contains(date)) {
			iw.setVisibility(View.VISIBLE);
		} else {
			iw.setVisibility(View.INVISIBLE);
		}
		
		return v;
	}
	public View setSelected(View view,int pos) {
		if (previousView != null) {
			previousView.setBackgroundResource(R.drawable.list_item_background);	
		}
		previousView = view;
		flag=pos;
		//view.setBackgroundColor(Color.parseColor("#a7a7a7"));
		view.setBackgroundResource(R.drawable.grey1);
		return view;
	}
	
	public void refreshDays() {
		// clear items
		items.clear();
		
		dayString.clear();
		Locale.setDefault(Locale.US);
		pmonth = (GregorianCalendar) month.clone();

		// month start day. ie; sun, mon, etc
		firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current month.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous month maximum day 31,30....
		calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three
		 * month's (previous,current,next) dates.
		 */
		pmonthmaxset = (GregorianCalendar) pmonth.clone();
		/**
		 * setting the start date as previous month's required date.
		 */
		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

		/**
		 * filling calendar gridview.
		 */
		for (int n = 0; n < mnthlength; n++) {

			itemvalue = df.format(pmonthmaxset.getTime());
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
			dayString.add(itemvalue);

		}
	}

	private int getMaxP() {
		int maxP;
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			pmonth.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}

}