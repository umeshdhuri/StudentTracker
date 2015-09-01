package com.appknetics.TrackChap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.appknetics.TrackChap.R;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class First extends Fragment {
	
	
	public static First newInstance(String title, String colorCode) {
		First pageFragment = new First();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("color code", colorCode);
		pageFragment.setArguments(bundle);
		return pageFragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v= inflater.inflate(R.layout.fragment_first, container, false);
		return v;
	}

}
