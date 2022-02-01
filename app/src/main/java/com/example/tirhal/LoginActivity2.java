package com.example.tirhal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class LoginActivity2 extends AppCompatActivity {
    EditText ed_email_login, ed_pass_login;
    Button btn_log;
    public static String userEmail="";
    public static String useName2;
    FirebaseAuth mAuth;
    String UserID ;
    List<Trip> tripsl;
    private FirebaseUser user;
    public static final String TAG = "Login";
    private TripDatabase database;
    private   DatabaseReference databaseRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for changing status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login2);
        ed_email_login = findViewById(R.id.edTxtEmailrregist);
        ed_pass_login = findViewById(R.id.edTxtPasswordregi);
        btn_log = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!= null){
                    userEmail = mAuth.getCurrentUser().getEmail();
                    UserID = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
                    Intent intent = new Intent(getApplicationContext(), FragmentMainActivity.class);
                    intent.putExtra("userID",UserID );
                    startActivity(intent);
                }
            }
        };
        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        database = Room.databaseBuilder(this, TripDatabase.class, "tripDB").build();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();


    }//end onCreate method

    private void loginUser() {
        String email = ed_email_login.getText().toString();
        String pass = ed_pass_login.getText().toString();
        if (TextUtils.isEmpty(email)) {
            ed_email_login.setError("email must be written");
            ed_email_login.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            ed_pass_login.setError("pass must be written");
            ed_pass_login.requestFocus();

        }else
        {
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity2.this, "user Login successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity2.this,FragmentMainActivity.class));
                    }else
                    {
                        Toast.makeText(LoginActivity2.this, "login Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, RegistrationActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);

    }
    public void onLoginClick2(View view)
    {
        startActivity(new Intent(this, FragmentMainActivity.class));

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Constants.LOG_TAG, "onActivityResult");
        if (requestCode == Constants.AUTH_REQUEST_CODE) {
            Log.i(Constants.LOG_TAG, "Auth request code is correct");
            if (resultCode == RESULT_OK) {
                Log.i(Constants.LOG_TAG, "signed in successfully");
                //check room isEmpty
                new check().execute();
                readOnFireBase();

            } else {
                Log.i(Constants.LOG_TAG, "not signed in successfully");
                finish();
            }
        }
    }
    private void readOnFireBase() {

        tripsl=new ArrayList<>();
        databaseRef.child("TripReminder").child("userID").child(FirebaseAuth.getInstance().getUid()).child("trips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i(TAG, "onDataChange: ");
                    //     Trip[] tripList = new Trip[(int) dataSnapshot.getChildrenCount()];
                    int i = 0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // TODO: handle the post
                        Trip trip = postSnapshot.getValue(Trip.class);
                        //           tripList[i] = trip;
                        tripsl.add(trip);
                        i++;
                    }
                    //         Log.i(TAG, "onDataChange: " + tripList.length);
                }
                insertTripsINRoom(tripsl);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
    public void insertTripsINRoom(List<Trip> trips) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "insertTripsINRoom: " + trips.size());
                for (int i = 0; i < trips.size(); i++) {
                    if(Calendar.getInstance().getTimeInMillis() > trips.get(i).getCalendar() && trips.get(i).getTripStatus().equals(Constants.UPCOMING_TRIP_STATUS)){
                        trips.get(i).setTripStatus(Constants.MISSED_TRIP_STATUS);
                    }
                    Trip trip = new Trip(trips.get(i).getUserID(), trips.get(i).getTripName(), trips.get(i).getStartPoint(),
                            trips.get(i).getStartPointLat(), trips.get(i).getStartPointLong(), trips.get(i).getEndPoint(),
                            trips.get(i).getEndPointLat(), trips.get(i).getEndPointLong(), trips.get(i).getDate(),
                            trips.get(i).getTime(), trips.get(i).getTripImg(), trips.get(i).getTripStatus(),
                            trips.get(i).getCalendar(), trips.get(i).getNotes());


                    database.tripDAO().insert(trip);
//                    if(trips.get(i).getTripStatus().equals(Constants.UPCOMING_TRIP_STATUS)){
//                        initAlarm(trips.get(i));
//                    }
                    Log.i(TAG, "insertTripsINRoom: " + trip.getTripName());
                }
            }
        }).start();
    }
    private class check extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            database.tripDAO().clear();
            //    readOnFireBase();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startActivity(new Intent(LoginActivity2.this, FragmentMainActivity.class));
            finish();
        }
    }


/* <item name="colorPrimary">@color/pu</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/white</item>
        <!-- Secondary brand color. -->
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_700</item>*/

}