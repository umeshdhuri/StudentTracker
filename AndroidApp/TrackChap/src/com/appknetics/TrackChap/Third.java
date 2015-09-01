package com.appknetics.TrackChap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.appknetics.TrackChap.R;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Third extends Fragment {

	public static Third newInstance(String title, String colorCode) {
		Third pageFragment = new Third();
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
		View v= inflater.inflate(R.layout.fragment_third, container, false);
		return v;
	}

}
