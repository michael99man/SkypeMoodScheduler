package com.github.michael99man.SkypeMoodScheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SkypeMessage {
	private static final String DATE_FORMAT = "MM/dd/yy (h:mm:ss a)";
	
	private String date;
	private String sender;
	private String message;
	
	
	public SkypeMessage(String message, String sender, String date){
		this.message = message;
		this.sender = sender;
		this.date = date;
		
		
	}
	
	public String formatString(){
		return date + " " + sender.toUpperCase() + " - " + message; 
	}
	
	private static String getTime() {
		return (new SimpleDateFormat(DATE_FORMAT).format(Calendar.getInstance()
				.getTime()));
	}
	
	public static SkypeMessage logNew(String message, String sender){
		return new SkypeMessage(message,sender,getTime());
	}
}
