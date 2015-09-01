package com.appknetics.TrackChap;




import com.appknetics.TrackChap.R;
import com.fragments.MyFragmentPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Tutorial extends FragmentActivity{
ViewPager viewPager;
Button bt_skip,bt_enter;
CheckBox chxBox;
CirclePageIndicator mIndicator;
int flag=2;
private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		  setContentView(R.layout.simple_circles);
	         mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);

		

	        ViewPager mPager = (ViewPager)findViewById(R.id.pager1);
			mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),flag);  
	
	        mPager.setAdapter(mMyFragmentPagerAdapter);

	        mIndicator.setViewPager(mPager);
	        final float density = getResources().getDisplayMetrics().density;
	      //  mIndicator.setBackgroundColor(0xFFCCCCCC);
	        mIndicator.setRadius(7 * density);
	        mIndicator.setPageColor(0x000000FF);
	        mIndicator.setFillColor(0xFF214385);
	        mIndicator.setStrokeColor(0xFF000000);
	        mIndicator.setStrokeWidth(1 * density);
	        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int index) {
					
					if(index==3)
						mIndicator.setVisibility(View.GONE);
					else
						mIndicator.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	}

}
