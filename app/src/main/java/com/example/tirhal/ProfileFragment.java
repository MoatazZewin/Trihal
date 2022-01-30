package com.example.tirhal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;




import java.util.ArrayList;
import java.util.List;



public class ProfileFragment extends Fragment {
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
//    private DatabaseReference databaseReference;
//    private TripDatabase database;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
//        imgProfilePhoto=view.findViewById(R.id.imgProfilePhoto);

    }
}
