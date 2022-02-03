package com.example.tirhal;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class FragmentAddTrip extends Fragment {

    EditText editTextTripName;
    EditText editTextStartPoint;
    EditText editTextEndPoint;
    TextView textViewTripName;
    TextView textViewDate;
    TextView textViewDate2;
    ImageButton btnAddNotes;
    Button btnSaveTrip;
    ImageButton imageButtonDate;
    ImageButton imageButtonDate2;
    TextView textViewTime;
    TextView textViewTime2;
    ImageButton imageButtonTime;
    ImageButton imageButtonTime2;
    RadioButton radioButtonOneDirection;
    RadioButton radioButtonRoundTrip;
    RadioGroup radioGroup;
    ConstraintLayout constraintLayoutRoundTrip;
    boolean isDateCorrect = false;
    boolean isTimeCorrect = false;
    boolean isDateCorrectRoundTrip = false;
    boolean isTimeCorrectRoundTrip = false;
    boolean isDateToday=false;
    boolean isDateTodayRoundTrip=false;
    boolean isFirstTimeSeleceted=false;
    boolean isFirstAddNotes=true;

    public static final String TAG = "AddTripFragment";
    private static final String apiKey = "AIzaSyDstMg0xbMmp5ciMBvnh1USuAQ0f3bTzGc";
    private static final int AUTOCOMPLETE_REQUEST_CODE_STARTPOINT = 1;
    private static final int AUTOCOMPLETE_REQUEST_CODE_ENDPOINT = 2;
    Calendar calender = Calendar.getInstance();
    final int year = calender.get(Calendar.YEAR);
    final int month = calender.get(Calendar.MONTH);
    final int day = calender.get(Calendar.DAY_OF_MONTH);
    String min;

    Boolean isRound = false;
    Place placeStartPoint;
    Place placeEndPoint;
    ArrayList<String> resultNotes;
    Trip trip;
    Trip selectedTrip;
    Calendar calenderNormal;
    Calendar calendarRound;
    public static final String PREF_NAME="MY_PREF";
    SharedPreferences myPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Places.initialize(getContext(), apiKey);

        myPref=getActivity().getSharedPreferences(PREF_NAME,0);

        Log.i(TAG, "onCreate: ");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");
        outState.putString("Date",textViewDate.getText().toString());
        outState.putString("Time",textViewTime.getText().toString());
        outState.putString("DateRound",textViewDate2.getText().toString());
        outState.putString("TimeRound",textViewTime2.getText().toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            textViewDate.setText(savedInstanceState.getString("Date"));
            textViewTime.setText(savedInstanceState.getString("Time"));
            textViewDate2.setText(savedInstanceState.getString("DateRound"));
            textViewTime2.setText(savedInstanceState.getString("TimeRound"));
        }
        Log.i(TAG, "onActivityCreated: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calenderNormal = Calendar.getInstance();
        calendarRound = Calendar.getInstance();
        if(savedInstanceState!=null){
            textViewDate.setText(savedInstanceState.getString("Date"));
            textViewTime.setText(savedInstanceState.getString("Time"));
            textViewDate2.setText(savedInstanceState.getString("DateRound"));
            textViewTime2.setText(savedInstanceState.getString("TimeRound"));
            Log.i(TAG, "onCreateView: ++");
        }

        Log.i(TAG, "onCreateView: --");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        editTextTripName = view.findViewById(R.id.ediTxt_tripName);
        textViewTripName = view.findViewById(R.id.txtView_tripName);
        textViewDate = view.findViewById(R.id.txtView_date);
        imageButtonDate = view.findViewById(R.id.btn_date);
        textViewTime = view.findViewById(R.id.txtview_time);
        imageButtonTime = view.findViewById(R.id.btn_time);
        editTextStartPoint = view.findViewById(R.id.editTxt_startPoint);
        editTextStartPoint.setFocusable(false);
        editTextEndPoint = view.findViewById(R.id.editTxt_endPoint);
        editTextEndPoint.setFocusable(false);
        textViewTime2 = view.findViewById(R.id.txtView_Time2);
        textViewDate2 = view.findViewById(R.id.txtView_Date2);
        imageButtonDate2 = view.findViewById(R.id.btn_date2);
        imageButtonDate2.setFocusable(false);
        imageButtonTime2 = view.findViewById(R.id.btn_time2);
        imageButtonTime2.setFocusable(false);
        radioGroup=view.findViewById(R.id.radioGroup_typeTrip);
        radioButtonOneDirection = view.findViewById(R.id.radioBtn_oneDirection);
        radioButtonRoundTrip = view.findViewById(R.id.radioBtn_roundTrip);
        btnSaveTrip = view.findViewById(R.id.btn_saveTrip);
        btnAddNotes = view.findViewById(R.id.btn_addNotes);
        constraintLayoutRoundTrip=view.findViewById(R.id.constraintLayoutAddRound);

        Log.i(TAG, "onCreateView: ");
        if(AddTripActivity.key==2) {
            btnSaveTrip.setText("Edit");
            radioGroup.setVisibility(View.GONE);
            constraintLayoutRoundTrip.setVisibility(View.GONE);
            btnAddNotes.setVisibility(View.GONE);

        }
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                resultNotes=new ArrayList();
                resultNotes = bundle.getStringArrayList("bundleKey");
                String date=bundle.getString("date");
                String time=bundle.getString("time");
                String date2=bundle.getString("date2");
                String time2=bundle.getString("time2");
                textViewDate.setText(date);
                textViewTime.setText(time);
                textViewDate2.setText(date2);
                textViewTime2.setText(time2);
                Log.i(TAG, "onFragmentResult: "+resultNotes+".."+date+".."+time+date2+".."+time2);
                // Do something with the result

            }
        });
        if (!isFirstAddNotes){
            btnAddNotes.setVisibility(View.GONE);
        }
        btnAddNotes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(isFirstAddNotes){
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setReorderingAllowed(true);
// Replace whatever is in the fragment_container view with this fragment
                    transaction.replace(R.id.fragmentB, FragmentAddNotes.class,null);
                    transaction.addToBackStack("name");
                    transaction. setReorderingAllowed(true);
                    transaction.commit();
                    // send data
                    //     if(!TextUtils.isEmpty(textViewTime.getText())||!TextUtils.isEmpty(textViewDate.getText())) {
                    Bundle result = new Bundle();
                    if (!TextUtils.isEmpty(textViewDate.getText()))
                        result.putString("date", textViewDate.getText().toString());
                    if (!TextUtils.isEmpty(textViewTime.getText()))
                        result.putString("time", textViewTime.getText().toString());
                    if (!TextUtils.isEmpty(textViewDate2.getText()))
                        result.putString("date2", textViewDate2.getText().toString());
                    if (!TextUtils.isEmpty(textViewTime2.getText()))
                        result.putString("time2", textViewTime2.getText().toString());
                    getParentFragmentManager().setFragmentResult("datakey", result);
                    Log.i(TAG, "onClick: addtrip" + result);
                    //       }
                    isFirstAddNotes=false;
                }
            }
        });
        radioButtonOneDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewTime2.setText("");
                textViewDate2.setText("");
                constraintLayoutRoundTrip.setVisibility(View.GONE);
                isRound = onRadioButtonClicked(v);
            }
        });
        radioButtonRoundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRound = onRadioButtonClicked(v)) {
                    Log.i(TAG, "onClick: " + isRound);
                    constraintLayoutRoundTrip.setVisibility(View.VISIBLE);
                }
            }
        });
        editTextStartPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesAutocompletes(AUTOCOMPLETE_REQUEST_CODE_STARTPOINT);
            }
        });
        editTextEndPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesAutocompletes(AUTOCOMPLETE_REQUEST_CODE_ENDPOINT);
            }
        });

        imageButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calenderDate(textViewDate, 1,calenderNormal);
                textViewTime.setText("");
            }
        });
        imageButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDateCorrect)
                    calenderTime(textViewTime, 1,calenderNormal);
                else
                    Toast.makeText(getContext(), "Please choose date first", Toast.LENGTH_SHORT).show();

                isFirstTimeSeleceted = true;
            }
        });
        imageButtonDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDateCorrect) {
                    calenderDate(textViewDate2, 2,calendarRound);
                    textViewTime2.setText("");
                }else
                    Toast.makeText(getContext(), "Please choose date of the first trip", Toast.LENGTH_SHORT).show();
            }
        });
        imageButtonTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDateCorrectRoundTrip && isFirstTimeSeleceted)
                    calenderTime(textViewTime2, 2,calendarRound);
                else
                    Toast.makeText(getContext(), "Please choose date of the round trip", Toast.LENGTH_SHORT).show();
            }
        });
        btnSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //know valid time

                checkData();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_STARTPOINT) {
            if (resultCode == RESULT_OK) {
                placeStartPoint = Autocomplete.getPlaceFromIntent(data);

                Log.i(TAG, "Place: " + placeStartPoint.getName() + ", " + placeStartPoint.getId());
                if (placeStartPoint.getLatLng() != null) {
                    editTextStartPoint.setText(placeStartPoint.getName());
                    Log.i(TAG, "Place: " + placeStartPoint.getLatLng());
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ENDPOINT) {
            if (resultCode == RESULT_OK) {
                placeEndPoint  = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + placeEndPoint.getName() + ", " + placeEndPoint.getId());
                editTextEndPoint.setText(placeEndPoint.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void placesAutocompletes(int flag) {
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                .build(getContext());
        startActivityForResult(intent, flag);
    }

    public void calenderDate(TextView textViewDate1, int check, Calendar incomingCal) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int nowYear;
                int nowMonth;
                int nowDay;
                month = month + 1;
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                if(check == 1) {
                    String[] dateTotal = date.split("-");
                    nowYear = Integer.valueOf(dateTotal[2]);
                    nowMonth = Integer.valueOf(dateTotal[1]);
                    nowDay = Integer.valueOf(dateTotal[0]);
                }
                else{
                    String oneTripDate = textViewDate.getText().toString();
                    String[] oneTripDateSplits = oneTripDate.split("-");
                    nowYear = Integer.valueOf(oneTripDateSplits[2]);
                    nowMonth = Integer.valueOf(oneTripDateSplits[1]);
                    nowDay = Integer.valueOf(oneTripDateSplits[0]);
                }
                if(year == nowYear && month == nowMonth && day== nowDay){
                    if(check==1)
                        isDateToday=true;
                    else
                        isDateTodayRoundTrip=true;
                }else{
                    if(check==1)
                        isDateToday=false;
                    else
                        isDateTodayRoundTrip=false;
                }
                if (year > nowYear) {
                    if (check == 1) {
                        isDateCorrect = true;
                    } else {
                        isDateCorrectRoundTrip = true;
                    }
                    //calnder
                    incomingCal.set(Calendar.DAY_OF_MONTH,day);
                    incomingCal.set(Calendar.MONTH,month-1);
                    incomingCal.set(Calendar.YEAR,year);
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    textViewDate1.setText(format.format(incomingCal.getTime()));
                }else if(year == nowYear){
                    if (month > nowMonth) {
                        if (check == 1) {
                            isDateCorrect = true;
                        } else {
                            isDateCorrectRoundTrip = true;
                        }
                        //calnder
                        incomingCal.set(Calendar.DAY_OF_MONTH, day);
                        incomingCal.set(Calendar.MONTH, month - 1);
                        incomingCal.set(Calendar.YEAR, year);
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        textViewDate1.setText(format.format(incomingCal.getTime()));
                    } else if(month == nowMonth){
                        if (day >= nowDay) {
                            if (check == 1) {
                                isDateCorrect = true;
                            } else {
                                isDateCorrectRoundTrip = true;
                            }
                            //calnder
                            incomingCal.set(Calendar.DAY_OF_MONTH,day);
                            incomingCal.set(Calendar.MONTH,month-1);
                            incomingCal.set(Calendar.YEAR,year);
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            textViewDate1.setText(format.format(incomingCal.getTime()));
                        } else {
                            Toast.makeText(getContext(), "Date is wrong", Toast.LENGTH_SHORT).show();
                            if (check == 1) {
                                isDateCorrect = false;
                            } else {
                                isDateCorrectRoundTrip = false;
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Date is wrong", Toast.LENGTH_SHORT).show();
                        if (check == 1) {
                            isDateCorrect = false;
                        } else {
                            isDateCorrectRoundTrip = false;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Date is wrong", Toast.LENGTH_SHORT).show();
                    if (check == 1) {
                        isDateCorrect = false;
                    } else {
                        isDateCorrectRoundTrip = false;
                    }
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void calenderTime(TextView textViewTime1, int check, Calendar incomingCal) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHours, int selectedMinute) {
                int nowHour;
                int nowMin;
                Log.i(TAG, "hours: " + selectedHours + " minutes: " + selectedMinute);
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm");
                String localTime = date.format(currentLocalTime);
                if(check==1){
                    String[] localTimeSplit = localTime.split(":");
                    nowHour = Integer.valueOf(localTimeSplit[0]);
                    nowMin =  Integer.valueOf(localTimeSplit[1]);
                }
                else{
                    String[] localTimeFirstTrip = textViewTime.getText().toString().split(":");
                    nowHour = Integer.valueOf(localTimeFirstTrip[0]);
                    nowMin = Integer.valueOf(localTimeFirstTrip[1]);
                }
                if (isDateToday || isDateTodayRoundTrip) {
                    if (selectedHours > nowHour) {
                        if (check == 1) {
                            isTimeCorrect = true;
                        } else {
                            isTimeCorrectRoundTrip = true;
                        }

                        incomingCal.set(Calendar.HOUR_OF_DAY,selectedHours);
                        incomingCal.set(Calendar.MINUTE,selectedMinute);
                        incomingCal.set(Calendar.SECOND,0);
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        textViewTime1.setText(format.format(incomingCal.getTime()));
                    } else {
                        if (selectedHours == nowHour) {
                            if (selectedMinute > nowMin) {
                                if (check == 1)
                                    isTimeCorrect = true;
                                else
                                    isTimeCorrectRoundTrip = true;

                                incomingCal.set(Calendar.HOUR_OF_DAY,selectedHours);
                                incomingCal.set(Calendar.MINUTE,selectedMinute);
                                incomingCal.set(Calendar.SECOND,0);
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                textViewTime1.setText(format.format(incomingCal.getTime()));

                            } else {
                                Toast.makeText(getContext(), "Time is not correct", Toast.LENGTH_SHORT).show();
                                if (check == 1)
                                    isTimeCorrect = false;
                                else
                                    isTimeCorrectRoundTrip = false;
                            }
                        } else {
                            Toast.makeText(getContext(), "Time is not correct", Toast.LENGTH_SHORT).show();
                            if (check == 1)
                                isTimeCorrect = false;
                            else
                                isTimeCorrectRoundTrip = false;
                        }
                    }
                }else{
                    incomingCal.set(Calendar.HOUR_OF_DAY,selectedHours);
                    incomingCal.set(Calendar.MINUTE,selectedMinute);
                    incomingCal.set(Calendar.SECOND,0);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    textViewTime1.setText(format.format(incomingCal.getTime()));
                    writeSp();
                }
            }
        }, 12, 0, false);
        timePickerDialog.show();
    }

    public Boolean onRadioButtonClicked(View view) {
        // Is the button now checked?
        //return true if checked is round
        boolean checked = ((RadioButton) view).isChecked();
        Boolean isRound = false;

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioBtn_oneDirection:
                if (checked)
                    break;
            case R.id.radioBtn_roundTrip:
                if (checked)
                    isRound = true;
                break;
        }
        Log.i(TAG, "onRadioButtonClicked: " + ((RadioButton) view).getText());
        return isRound;
    }

    public void checkData(){
        Log.i(TAG, "checkData: ");
        if(!TextUtils.isEmpty(editTextTripName.getText())){
            editTextTripName.setError(null);
            if(!TextUtils.isEmpty(editTextStartPoint.getText())){
                editTextStartPoint.setError(null);
                if(!TextUtils.isEmpty(editTextEndPoint.getText())){
                    editTextEndPoint.setError(null);
                    if(!TextUtils.isEmpty(textViewDate.getText())){
                        textViewDate.setError(null);
                        if(!TextUtils.isEmpty(textViewTime.getText())){
                            textViewTime.setError(null);

                            //when edit trip
                            if(AddTripActivity.key==2){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (placeEndPoint==null) {
                                            FragmentMainActivity.database.tripDAO().EditTrip(AddTripActivity.ID, editTextTripName.getText().toString(), editTextStartPoint.getText().toString(),
                                                    editTextEndPoint.getText().toString(), selectedTrip.getEndPointLat(),
                                                    selectedTrip.getEndPointLong(), textViewDate.getText().toString(), textViewTime.getText().toString(),calenderNormal.getTimeInMillis());
                                            getActivity().finish();


                                            Log.i(TAG, "run: place end null");
                                        }else {
                                            FragmentMainActivity.database.tripDAO().EditTrip(AddTripActivity.ID, editTextTripName.getText().toString(), editTextStartPoint.getText().toString(),
                                                    editTextEndPoint.getText().toString(), placeEndPoint.getLatLng().latitude
                                                    , placeEndPoint.getLatLng().longitude, textViewDate.getText().toString(), textViewTime.getText().toString(),calenderNormal.getTimeInMillis());
                                            getActivity().finish();
                                            Log.i(TAG, "run: place end  not null");
                                        }
                                    }
                                }).start();
                                return;
                            }
                            // add trip object
                            if (resultNotes != null) {

                                trip = new Trip(FragmentMainActivity.fireBaseUseerId, editTextTripName.getText().toString(), placeStartPoint.getName(), placeStartPoint.getLatLng().latitude,
                                        placeStartPoint.getLatLng().longitude, placeEndPoint.getName(), placeEndPoint.getLatLng().latitude, placeEndPoint.getLatLng().longitude,
                                        textViewDate.getText().toString(), textViewTime.getText().toString(), R.drawable.preview,
                                        "upcoming", myPref.getLong("CalendarNormal", 0), resultNotes);
                                if (isRound) {
                                    if (!TextUtils.isEmpty(textViewDate2.getText())) {
                                        textViewDate2.setError(null);
                                        if (!TextUtils.isEmpty(textViewTime2.getText())) {
                                            textViewTime2.setError(null);
                                            //create two obj
                                            Trip tripRound = new Trip(FragmentMainActivity.fireBaseUseerId, editTextTripName.getText().toString() + " Round", placeEndPoint.getName(), placeEndPoint.getLatLng().latitude, placeEndPoint.getLatLng().longitude,
                                                    placeStartPoint.getName(), placeStartPoint.getLatLng().latitude, placeStartPoint.getLatLng().longitude,
                                                    textViewDate2.getText().toString(), textViewTime2.getText().toString(), R.drawable.preview,
                                                    "upcoming", calendarRound.getTimeInMillis(), null);
                                            insertRoom(trip);
                                            insertRoom(tripRound);
                                            getActivity().finish();
                                        } else {
                                            textViewTime2.setError("Valid Time");
                                            Toast.makeText(getContext(), "Please, Enter Valid Time for round trip", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        textViewDate2.setError("Valid Date");
                                        Toast.makeText(getContext(), "Please, Enter Valid Date for round trip", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    insertRoom(trip);
                                    getActivity().finish();
                                }
                            }else{
                                trip = new Trip(FragmentMainActivity.fireBaseUseerId, editTextTripName.getText().toString(), placeStartPoint.getName(), placeStartPoint.getLatLng().latitude,
                                        placeStartPoint.getLatLng().longitude, placeEndPoint.getName(), placeEndPoint.getLatLng().latitude, placeEndPoint.getLatLng().longitude,
                                        textViewDate.getText().toString(), textViewTime.getText().toString(), R.drawable.preview,
                                        "upcoming", calenderNormal.getTimeInMillis(), null);
                                if (isRound) {
                                    if (!TextUtils.isEmpty(textViewDate2.getText())) {
                                        textViewDate2.setError(null);
                                        if (!TextUtils.isEmpty(textViewTime2.getText())) {
                                            textViewTime2.setError(null);
                                            //create two obj
                                            Trip tripRound = new Trip(FragmentMainActivity.fireBaseUseerId, editTextTripName.getText().toString() + " Round", placeEndPoint.getName(), placeEndPoint.getLatLng().latitude, placeEndPoint.getLatLng().longitude,
                                                    placeStartPoint.getName(), placeStartPoint.getLatLng().latitude, placeStartPoint.getLatLng().longitude,
                                                    textViewDate2.getText().toString(), textViewTime2.getText().toString(), R.drawable.preview,
                                                    "upcoming", calendarRound.getTimeInMillis(), null);
                                            insertRoom(trip);
                                            insertRoom(tripRound);
                                            getActivity().finish();

                                        } else {
                                            textViewTime2.setError("Invalid Time");
                                            Toast.makeText(getContext(), "Please, Enter Valid Time for round trip", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        textViewDate2.setError("Invalid Date");
                                        Toast.makeText(getContext(), "Please, Enter Valid Date for round trip", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    insertRoom(trip);
                                    getActivity().finish();
                                }
                            }

                        }else{
                            textViewTime.setError("Valid Time");
                            Toast.makeText(getContext(),"Please, Enter Valid Time",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        textViewDate.setError("Please Enter Valid Date");
                        Toast.makeText(getContext(),"Please, Enter Valid Date",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    editTextStartPoint.setError(" Required End Point");
                    Toast.makeText(getContext(),"Please, Required End Point",Toast.LENGTH_SHORT).show();
                }

            }else{
                editTextEndPoint.setError("Please Enter Valid Start Point");
                Toast.makeText(getContext(),"Please, Required Start Point",Toast.LENGTH_SHORT).show();
            }
        }else{
            editTextTripName.setError("Required");
            Toast.makeText(getContext(),"Please, Required Trip Name",Toast.LENGTH_SHORT).show();

        }
    }

    public void insertRoom(Trip trip) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FragmentMainActivity.database.tripDAO().insert(trip);
                 getActivity().finish(); //added by amr
            }
        }).start();
        Log.i(TAG, "insertRoom: ");
    }

    private class LoadRoomData extends AsyncTask<Void, Void, Trip> {

        @Override
        protected Trip doInBackground(Void... voids) {
            return FragmentMainActivity.database.tripDAO().selectById(AddTripActivity.ID);
        }
        @Override
        protected void onPostExecute(Trip trip) {
            super.onPostExecute(trip);
            selectedTrip = trip;
            if (selectedTrip!=null) {
                editTextTripName.setText(selectedTrip.getTripName());
                editTextStartPoint.setText(selectedTrip.getStartPoint());
                editTextEndPoint.setText(selectedTrip.getEndPoint());
                // textViewDate.setText(selectedTrip.getDate());
                //   textViewTime.setText(selectedTrip.getTime());
            }
            Log.i(TAG, "onPostExecute: "+selectedTrip);
        }
    }
    public void writeSp(){
        SharedPreferences.Editor editor=myPref.edit();
        editor.putLong("CalendarNormal",calenderNormal.getTimeInMillis());
        editor.putLong("CalendarRound",calendarRound.getTimeInMillis());
        editor.commit();

    }


}