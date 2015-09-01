package com.appknetics.TrackChap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import com.appknetics.TrackChap.R;
/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Second extends Fragment {

	public static Second newInstance(String title, String colorCode) {
		Second pageFragment = new Second();
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
		View v= inflater.inflate(R.layout.fragment_second, container, false);
		return v;
	}

}
