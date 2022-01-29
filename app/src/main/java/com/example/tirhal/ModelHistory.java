package com.example.tirhal;

public class ModelHistory {
    String timeOfTrip;
    String TypeOfTrip;
    String fromTrip;
    String toTrip;

    public ModelHistory(String timeOfTrip, String typeOfTrip, String fromTrip, String toTrip) {
        this.timeOfTrip = timeOfTrip;
        TypeOfTrip = typeOfTrip;
        this.fromTrip = fromTrip;
        this.toTrip = toTrip;
    }

    public String getTimeOfTrip() {
        return timeOfTrip;
    }

    public void setTimeOfTrip(String timeOfTrip) {
        this.timeOfTrip = timeOfTrip;
    }

    public String getTypeOfTrip() {
        return TypeOfTrip;
    }

    public void setTypeOfTrip(String typeOfTrip) {
        TypeOfTrip = typeOfTrip;
    }

    public String getFromTrip() {
        return fromTrip;
    }

    public void setFromTrip(String fromTrip) {
        this.fromTrip = fromTrip;
    }

    public String getToTrip() {
        return toTrip;
    }

    public void setToTrip(String toTrip) {
        this.toTrip = toTrip;
    }
}
