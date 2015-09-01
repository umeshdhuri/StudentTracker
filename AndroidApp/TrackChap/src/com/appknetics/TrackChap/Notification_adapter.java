package com.appknetics.TrackChap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appknetics.TrackChap.R;
import com.fragments.Delete_interface;
import com.fragments.Noti_uti;


public class Notification_adapter extends BaseAdapter {
    // The list of videos to display
	int position1;
	Context context;
	private Cursor cursor;
	String flag;
    private LayoutInflater mInflater;
    ArrayList<Noti_uti> notilist;
    Noti_uti uti;
	private String title;
	Delete_interface delete_int;
    public Notification_adapter(Context activity,
			ArrayList<Noti_uti> notilist, Delete_interface touch) {
		// TODO Auto-generated constructor stub 
    	this.notilist=notilist;
    	delete_int=touch;
    	this.mInflater = LayoutInflater.from(activity);
	}
	
    @Override
    public int getCount() {
        return notilist.size();
    }
 
    @Override
    public Object getItem(int position) {
        return notilist.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 public class Holder{
	TextView title,duration; 
	ImageButton delete;
 }
@SuppressLint("InflateParams") @Override
public View getView(final int position, View convertView, ViewGroup parent) {	
	final Holder holder;
    if(convertView == null){         
        convertView = mInflater.inflate(R.layout.notification_row,null);
        holder=new Holder();
        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.duration = (TextView) convertView.findViewById(R.id.duration);
        holder.delete = (ImageButton) convertView.findViewById(R.id.delete);
        convertView.setTag(holder);
        }else{       
        	holder=(Holder)convertView.getTag();
        }
    uti=notilist.get(position);
    holder.title.setText(uti.getTitle());   
    holder.duration.setText(uti.getDuration());
    
    holder.delete.setOnClickListener(new OnClickListener() {
		
		private String duration;

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			title=notilist.get(position).getTitle().trim();
			duration=notilist.get(position).getDuration().trim();
			delete_int.delete(duration);
		}
	});
   
	return convertView;
}



}