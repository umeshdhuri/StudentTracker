package com.fragments;

import com.appknetics.TrackChap.First;
import com.appknetics.TrackChap.Four;
import com.appknetics.TrackChap.R;
import com.appknetics.TrackChap.Second;
import com.appknetics.TrackChap.Third;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


public  class MyFragmentPagerAdapter extends FragmentPagerAdapter 
{  
	int flag;
    public MyFragmentPagerAdapter(FragmentManager fm, int flag) 
    {  
         super(fm);  
         this.flag=flag;
    }  

	@Override  
    public Fragment getItem(int index) 
    {
		if(index==0)
		{
			return First.newInstance("Login1", "Login Activity1");		
			
		}
		if(index==1)
		{
			return Second.newInstance("Login2", "Login Activity2");		
			
		}
		if(index==2)
		{
			return Third.newInstance("Login3", "Login Activity3");		
			
		}
		if(index==3)
		{
			Log.d("==flag","=--="+flag);
			return Four.newInstance("Login4", "Login Activity4",flag);		
			
		}else{
			return Four.newInstance("Login5", "Login Activity5",flag);	
		}
    }  

	@Override  
    public int getCount() 
    {
		return 4;
    	
    }
}
