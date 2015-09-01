package com.appknetics.TrackChap;

import com.appknetics.TrackChap.R;
import com.viewpagerindicator.CirclePageIndicator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class ViewPageAdapter extends PagerAdapter {
	Context context;
	String[] quote;
	LayoutInflater inflater;
	View itemView;
	int flag=0;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	CirclePageIndicator mIndicator;
	String notcome_flag;
	@SuppressWarnings("static-access")
	public ViewPageAdapter(Context context,String quote[])
	{
		this.context = context;
		this.quote = quote;
		this.mIndicator=mIndicator;
		preferences=context.getSharedPreferences("tracker",context.MODE_PRIVATE);
		editor=preferences.edit();
		
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView((RelativeLayout) object);
		
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
	
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(position==0)
		{
		 itemView = inflater.inflate(R.layout.viewpager, container,
				false);
		}
		else if(position==1)
		{
			itemView = inflater.inflate(R.layout.welcomepage2, container,
					false);

			
		}
		else if(position==2)
		{
			itemView = inflater.inflate(R.layout.welcomepage3, container,
					false);

		}else if(position==3)
		{
			 
			itemView = inflater.inflate(R.layout.fragment_four_tour, container,
					false);
					
		}
		else
		{
			
			
		}
		
		((ViewPager) container).addView(itemView);
		return itemView;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == ((RelativeLayout) object);
	}
	
	

}
