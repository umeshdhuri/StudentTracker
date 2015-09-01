package com.appknetics.TrackChap;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Static_class {
	
	
	static Calendar c = Calendar.getInstance();
	 final double lat=0.0;
	 static double latitude=0.0;
	
	public void Staic_class(){
		
	}
	public static String datefun(){		
		 int day = c.get(Calendar.DAY_OF_MONTH);
	        int month = c.get(Calendar.MONTH);
	        int year = c.get(Calendar.YEAR);
	        String newdate = day+"/"+month+"/"+year;
	        return newdate;
	}
	public static String timefun(){
		
        int seconds = c.get(Calendar.SECOND);
        int minutes = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);
        String newtime = hour+":"+minutes+":"+seconds;		
		return newtime;		
	}

	public static Editor sharedp(String filename,Context context){
		SharedPreferences preferences=context.getSharedPreferences(filename,context.MODE_PRIVATE);
		SharedPreferences.Editor e=preferences.edit();
		return e;		
	}
	public static void PutInt(Editor e,String key,int value){
		e.putInt(key,value);
		e.commit();
	}
	public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-6])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$)$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
	public static boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
}
