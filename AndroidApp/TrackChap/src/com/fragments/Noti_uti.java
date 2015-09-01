package com.fragments;

import java.io.Serializable;

public class Noti_uti implements Serializable {
	
	private static final long serialVersionUID = 1400190218204587053L;
	
	private String duration,title,uid;

	
	public Noti_uti(String uid, String title,String duration) {
		super();
		this.title = title;
		this.uid=uid;
		this.duration =duration;
	}
	public String getTitle() {
		return title;
	}

	public String getDuration() {
		return duration;
	}
	public String getuid() {
		return uid;
	}
	
}