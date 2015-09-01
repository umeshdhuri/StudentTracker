package com.appknetics.TrackChap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import com.appknetics.TrackChap.R;


public class Four extends Fragment {
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	private String notcome_flag;
	 int flag;
	public static Four newInstance(String title, String colorCode, int flag) {
		Four pageFragment = new Four();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("color code", colorCode);
		pageFragment.setArguments(bundle);
		pageFragment.flag=flag;
		return pageFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v= inflater.inflate(R.layout.fragment_four, container, false);
		
		
		CheckBox notcome=(CheckBox)v.findViewById(R.id.checkBox1);
		Button Signin=(Button)v.findViewById(R.id.Signin);
		
		//take a tour inside the application
		if(flag==1){
			notcome.setVisibility(View.GONE);
			Signin.setVisibility(View.GONE);
		}else{
			notcome.setVisibility(View.VISIBLE);
			Signin.setVisibility(View.VISIBLE);
		}
		
		preferences=getActivity().getSharedPreferences("tracker",getActivity().MODE_PRIVATE);
		editor=preferences.edit();
		Typeface confirm_font = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Book.otf");
		notcome.setTypeface(confirm_font);
		notcome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((CheckBox) v).isChecked()) {
					
					editor.putString("notcome_flag", "1");
					editor.commit();
				}else{
					editor.putString("notcome_flag", "2");
					editor.commit();
				}
			}
		});
		
		Signin.setTypeface(confirm_font);
		 notcome_flag=preferences.getString("status","");
		if(notcome_flag.equals("true")){	
			
			Signin.setText("Enter into App");
							
		}		
		Signin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(notcome_flag.equals("true")){	
					Intent i=new Intent(getActivity(),HomeActivity.class);
					startActivity(i);	
					getActivity().finish();
				}else{
				Intent i=new Intent(getActivity(),Login_Activity.class);
				startActivity(i);
				getActivity().finish();
				}
				
				
			}
		});
	
		
		return v;
	}

}
