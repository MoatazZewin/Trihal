package com.example.tirhal;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Trip.class},version = 1,exportSchema = false)
public abstract class TripDatabase extends RoomDatabase {

    public abstract TripDAO tripDAO();
}
