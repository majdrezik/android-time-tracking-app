package com.example.hourstracker.ui.Models;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Shift {
	private static int GlobalCounter;

	public String getGUID() {
		return GUID;
	}

	private String GUID;
	private Date startTime;
	private Date endTime;
	private String Notes;
	private double startLongitude;
	private double startLatitude;

public Shift(){
	GUID = String.format("%09d",GlobalCounter);

	GlobalCounter++;
}
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(double endLatitude) {
		this.endLatitude = endLatitude;
	}

	private double endLongitude;
	private double endLatitude;
	public double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(double startLatitude) {
		this.startLatitude = startLatitude;
	}





	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

	public Time getTotalHours() {
		return this.CalculateTotalHours();
	}



	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.S");

		return "{" +
				"startTime:'" + dateFormat.format(startTime)+ "'"+
				", endTime:'" + dateFormat.format(endTime) +"'"+
				", Notes:'" + Notes + '\'' +
				", startLongitude:" + startLongitude +
				", startLatitude:" + startLatitude +
				", endLongitude:" + endLongitude +
				", endLatitude:" + endLatitude +
				'}';
	}

	private Time CalculateTotalHours(){
		if(endTime==null || startTime==null){
			return null;
		}
		Time totalHours = new Time();
		long diffMS = endTime.getTime()-startTime.getTime();
		long diff = diffMS/1000; // parse to seconds
		long diffMinutes = diff/60; // parse to minutes
		int hours = (int)diffMinutes/60;
		int minutes =(int)(diffMinutes -hours*60)%60;
		totalHours.hours=hours;
		totalHours.minutes=minutes;
		totalHours.seconds =(int)diff%60;
		return totalHours;
	}


	public String getFromLocationName(Context context){
		if(startLatitude==0 || startLongitude==0 ){
			return "No location";
		}
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(startLatitude, startLongitude, 1);
			Address obj = addresses.get(0);
			String add = obj.getAddressLine(0);
			add = add + "\n" + obj.getCountryName();
			add = add + "\n" + obj.getCountryCode();
			add = add + "\n" + obj.getAdminArea();
			add = add + "\n" + obj.getPostalCode();
			add = add + "\n" + obj.getSubAdminArea();
			add = add + "\n" + obj.getLocality();
			add = add + "\n" + obj.getSubThoroughfare();

			Log.v("IGA", "Address" + add);
			return add;
			// Toast.makeText(this, "Address=>" + add,
			// Toast.LENGTH_SHORT).show();

			// TennisAppActivity.showDialog(add);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
			return "long:"+String.format("%03.4ld",startLongitude)+"lat:"+String.format("%03.4ld",startLatitude);
		}
	}
}
