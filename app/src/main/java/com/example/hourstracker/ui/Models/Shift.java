package com.example.hourstracker.ui.Models;

import java.sql.Time;


import java.util.Date;

public class Shift {

	private Time startTime;
	private Time endTime;
	private Date shiftDate;
	private String Notes;
	private int totalHours;
	private int startLongitude;
	private int startLatitude;

	public int getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(int endLongitude) {
		this.endLongitude = endLongitude;
	}

	public int getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(int endLatitude) {
		this.endLatitude = endLatitude;
	}

	private int endLongitude;
	private int endLatitude;
	public int getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(int startLongitude) {
		this.startLongitude = startLongitude;
	}

	public int getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(int startLatitude) {
		this.startLatitude = startLatitude;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Date getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(Date shiftDate) {
		this.shiftDate = shiftDate;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}

	public int getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(int totalHours) {
		this.totalHours = totalHours;
	}



}
