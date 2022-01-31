package com.example.tirhal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;

@Entity(tableName = "Trip")
public class Trip  {

    @NonNull
    private String userID;

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String tripName;
    @NonNull
    private String startPoint;
    @NonNull
    private double startPointLat;
    @NonNull
    private double startPointLong;
    @NonNull
    private String endPoint;

    public double getStartPointLat() {
        return startPointLat;
    }

    public void setStartPointLat(double startPointLat) {
        this.startPointLat = startPointLat;
    }

    public double getStartPointLong() {
        return startPointLong;
    }

    public void setStartPointLong(double startPointLong) {
        this.startPointLong = startPointLong;
    }

    @NonNull
    private double endPointLat;
    @NonNull
    private double endPointLong;
    @NonNull
    private String date;
    @NonNull
    private String time;
    @NonNull
    private int tripImg;
    @NonNull
    private String tripStatus;
    @TypeConverters(DataConverter.class)
    private ArrayList<String> notes;
    @NonNull
    private long calendar;



    public Trip(@NonNull String userID, @NonNull String tripName, @NonNull String startPoint,@NonNull double startPointLat, @NonNull double startPointLong,
                @NonNull String endPoint, @NonNull double endPointLat, @NonNull double endPointLong,
                @NonNull String date, @NonNull String time, @NonNull int tripImg, String tripStatus, @NonNull long calendar,ArrayList<String> notes) {
        this.userID = userID;
        this.calendar = calendar;
        this.id = id;
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.startPointLat=startPointLat;
        this.startPointLong=startPointLong;
        this.endPoint = endPoint;
        this.endPointLat = endPointLat;
        this.endPointLong = endPointLong;
        this.date = date;
        this.time = time;
        this.tripImg = tripImg;
        this.tripStatus = tripStatus;
        this.notes=notes;
    }


    public Trip(){}

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripImg() {
        return tripImg;
    }

    public void setTripImg(int tripImg) {
        this.tripImg = tripImg;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getEndPointLat() {
        return endPointLat;
    }

    public void setEndPointLat(double endPointLat) {
        this.endPointLat = endPointLat;
    }

    public double getEndPointLong() {
        return endPointLong;
    }

    public void setEndPointLong(double endPointLong) {
        this.endPointLong = endPointLong;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public long getCalendar() {
        return calendar;
    }

    public void setCalendar(long calendar) {
        this.calendar = calendar;
    }


}
