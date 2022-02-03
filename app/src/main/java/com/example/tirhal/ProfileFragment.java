package com.example.tirhal;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.room.Room;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;



public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;
    ImageView imgLogout;
    ImageView imgSync;
    ImageView imgAboutAs;
    ImageView imgHowToUse;
    ImageView imgTripReport;
    TextView txtLogout;
    TextView txtSync;
    TextView txtAboutAs;
    TextView txtHowToUse;
    TextView txtTripsReport;
    TextView txtName;
    TextView txtEmail;
    ImageView imgProfilePhoto;
    String reportTripMessage;
    public static final String TAG="profile";
   private DatabaseReference databaseReference;
   private TripDatabase database;
    private String fireBaseUseerId;
    List<Trip> trips = new ArrayList<>();
    boolean isSuccess=false;
    boolean isSync=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState ){
        return inflater.inflate(R.layout.fragment_profile, container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        database = Room.databaseBuilder(getContext(), TripDatabase.class, "tripDB").build();
        fireBaseUseerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetCountTrips().execute();
        imgLogout = view.findViewById(R.id.imgLogout);
        imgSync = view.findViewById(R.id.imgSync);
        imgHowToUse = view.findViewById(R.id.imgHowToUse);
        imgAboutAs = view.findViewById(R.id.imgAboutAs);
        imgTripReport=view.findViewById(R.id.imgTripsReport);
        txtLogout = view.findViewById(R.id.txtLogout);
        txtSync = view.findViewById(R.id.txtSync);
        txtHowToUse = view.findViewById(R.id.txtHowToUse);
        txtAboutAs = view.findViewById(R.id.txtAboutAs);
        txtTripsReport = view.findViewById(R.id.txtTripsReport);
        txtName = view.findViewById(R.id.name_txt);
        txtEmail =view.findViewById(R.id.email_txt);
        txtEmail.setText(LoginActivity2.userEmail);
        txtName.setText(LoginActivity2.useName2);

        initializeLogOut();
        getTripsReport();
        intializeAboutUs();
        intializeHowToUse();

//        imgProfilePhoto=view.findViewById(R.id.imgProfilePhoto);
        imgSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSync=true;
                new readData().execute();
                writeOnFireBase(trips);
            }
        });
        txtSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSync=true;
                new readData().execute();
                writeOnFireBase(trips);
            }
        });

    }
    public void initializeLogOut() {
        mAuth = FirebaseAuth.getInstance();
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mAuth.signOut();
                        new UnregisterData().execute();
                        new readData().execute();
                        writeOnFireBase(trips);
                        startActivity(new Intent(getContext(), LoginActivity2.class));
                        getActivity().finish();
                    }
                });
//                getActivity().finish();

            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mAuth.signOut();
                        new UnregisterData().execute();
                        new readData().execute();
                        writeOnFireBase(trips);
                        startActivity(new Intent(getContext(), LoginActivity2.class));
                        getActivity().finish();
                    }
                });
            }
        });
    }
    private class UnregisterData extends AsyncTask<Void, Void, List<Trip>> {

        @Override
        protected List<Trip> doInBackground(Void... voids) {
            return FragmentMainActivity.database.tripDAO().selectUpcomingTrip(FragmentMainActivity.fireBaseUseerId, "upcoming");
        }
        @Override
        protected void onPostExecute(List<Trip> trips) {
            super.onPostExecute(trips);
//            for(int i=0;i<trips.size();i++){
//                if(trips.get(i).getTripStatus().equals("upcoming")){
//                    unregisterAlarm(trips.get(i));
//                }
//            }
        }
    }// class UnregisterData
    public void writeOnFireBase(List<Trip>trips){
        if(isNetworkAvailable(getContext())) {
            Trip trip;
            databaseReference.child("Tirhal").child("userID").child(fireBaseUseerId).child("trips").removeValue();

            for (int i = 0; i < trips.size(); i++) {
                trip = new Trip(trips.get(i).getUserID(),trips.get(i).getTripName(),trips.get(i).getStartPoint(),
                        trips.get(i).getStartPointLat(),trips.get(i).getStartPointLong(),trips.get(i).getEndPoint(),
                        trips.get(i).getEndPointLat(),trips.get(i).getEndPointLong(),trips.get(i).getDate(),
                        trips.get(i).getTime(),trips.get(i).getTripImg(),trips.get(i).getTripStatus(),
                        trips.get(i).getCalendar(), trips.get(i).getNotes());
                Log.i(TAG, "writeOnFireBase: " + trip.getTripName() + trip.getId() + trip.getStartPoint()+trip.getNotes());
                databaseReference.child("Tirhal").child("userID").child(fireBaseUseerId).child("trips").push().setValue(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        task.addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if(isSync){
                                    isSuccess=true;
                                }
                            }
                        });
                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(!isSync){
                                    isSuccess=false;
                                }
                            }
                        });
                    }
                });
            }
            if(isSuccess){
                Toast.makeText(getContext(), "Synchronization is completed successfully", Toast.LENGTH_SHORT).show();
                isSync=false;
            }
        }
        else{
            Toast.makeText(getContext(), "No Internet ", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, "writeOnFireBase: ");
    }
    public static boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private class readData extends AsyncTask<Void, Void, List<Trip>> {
        @Override
        protected List<Trip> doInBackground(Void... voids) {
            return  database.tripDAO().selectAll(fireBaseUseerId);
        }
        @Override
        protected void onPostExecute(List<Trip> tripsl) {
            super.onPostExecute(trips);
            Log.i(TAG, "onPostExecute: "+trips);
            trips=tripsl;
        }
    }


    private void openDaialog(String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_warning, (ConstraintLayout) getActivity().findViewById(R.id.dialogLayoutContainer));
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(Constants.APP_NAME);
        ((TextView) view.findViewById(R.id.textMessage)).setText(message);
        ((Button) view.findViewById(R.id.btnOk)).setText(Constants.PER_DIALOG_OK);
        ((ImageView) view.findViewById(R.id.imgTitle)).setImageResource(R.drawable.ic_baseline_warning_24);

        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();


        view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() !=null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    private void getTripsReport() {
        txtTripsReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaialog(reportTripMessage);
            }
        });

        imgTripReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaialog(reportTripMessage);
            }
        });
    }
    private void intializeAboutUs() {
        txtAboutAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaialog("This Application is made by Group One ITI intake 42 as project for android Course \nMade By \n \nNorhan Sayed \nAlaa FAyad \nMoataz Ahmed");
            }
        });

        imgAboutAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaialog("This Application is made by Group One ITI intake 41 as project for android Course \nMade By \nNorhan Sayed \nAlaa FAyad \nMoataz Ahmed");
            }
        });
    }
    private void intializeHowToUse() {
        txtHowToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaialog("This application helps you to schedule your trips by addind trip from upcoming window and can delete any trip by long pressing on it so it be transferred to" +
                        "Histroy window and can show finished trips routes on google maps");
            }
        });

        imgHowToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaialog("This application helps you to schedule your trips by addind trip from upcoming window and can delete any trip by long pressing on it so it be transferred to" +
                        "Histroy window and can show finished trips routes on google maps");
            }
        });
    }
    class GetCountTrips extends AsyncTask<Void, Void,String> {

        @Override
        protected  String doInBackground(Void... voids) {
            String reportMsg = " Total of upcoming trips : ";
            reportMsg += FragmentMainActivity.database.tripDAO().getCountTripType(FragmentMainActivity.fireBaseUseerId, "upcoming");
            reportMsg +="\n Total of finished trips : ";
            reportMsg += FragmentMainActivity.database.tripDAO().getCountTripType(FragmentMainActivity.fireBaseUseerId, "finished");
            reportMsg +="\n Total of cancelled trips : ";
            reportMsg += FragmentMainActivity.database.tripDAO().getCountTripType(FragmentMainActivity.fireBaseUseerId, "cancelled");
            reportMsg +="\n Total of missed trips : ";
            reportMsg += FragmentMainActivity.database.tripDAO().getCountTripType(FragmentMainActivity.fireBaseUseerId, "missed");
            return reportMsg;
        }

        @Override
        protected void onPostExecute(String reportMsg) {
            super.onPostExecute(reportMsg);
            reportTripMessage=reportMsg;

        }
    }
}
