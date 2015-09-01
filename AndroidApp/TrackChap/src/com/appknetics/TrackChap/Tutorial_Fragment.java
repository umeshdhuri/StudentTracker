package com.appknetics.TrackChap;

import com.fragments.Constantclass;
import com.fragments.MyFragmentPagerAdapter;

import com.viewpagerindicator.CirclePageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Tutorial_Fragment extends Fragment {
	ViewPager viewPager;
	Button bt_skip,bt_enter;
	CheckBox chxBox;
	CirclePageIndicator mIndicator;
	private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
	int flag=1;

	public Tutorial_Fragment() {
		// Required empty public constructor
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

		View v= inflater.inflate(R.layout.fragment_tutorial_, container, false);
		
		 mIndicator = (CirclePageIndicator)v.findViewById(R.id.indicator);
		 String[] quote=new String[]{"Protect my Card looks like your phone�s contact list. It provides names of all major UK card providers. If you lose you cards you can contact you card providers instantly.This reduces the time that you and your cards are exposed to fraudaddition we provide you with information on the ways that frausters are trying to trick you. ","xyz","hello","how","are"};

		 ViewPageAdapter mAdapter=new ViewPageAdapter(getActivity(), quote);


	        ViewPager mPager = (ViewPager)v.findViewById(R.id.pager1);
			mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(),flag);  
	
	        mPager.setAdapter(mAdapter);

	        mIndicator.setViewPager(mPager);
	        final float density = getResources().getDisplayMetrics().density; 
	      //  mIndicator.setBackgroundColor(0xFFCCCCCC);
	        mIndicator.setRadius(7 * density);
	        mIndicator.setPageColor(0x000000FF);
	        mIndicator.setFillColor(0xFF214385);
	        mIndicator.setStrokeColor(0xFF000000);
	        mIndicator.setStrokeWidth(1 * density);
		return v;
	}

}
