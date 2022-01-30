package com.example.tirhal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tirhal.Adapters.AdapterAddNote;

import java.util.ArrayList;


public class FragmentAddNotes extends Fragment {

    RecyclerView recyclerView;
    AdapterAddNote adapter;
    LinearLayoutManager linearLayoutManager;
    ImageButton btnAddNote;
    ImageButton   btnDeleteNote;
    Button btnSaveNotes;
    ArrayList<String> notes;
    String date;
    String time;
    String date2;
    String time2;
    Bundle result;

    ArrayList<String> selectedNotes;
    public static final String TAG="Notes";

    public FragmentAddNotes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("datakey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                if(bundle!=null){
                    date = bundle.getString("date");
                    time=bundle.getString("time");
                    date2 = bundle.getString("date2");
                    time2=bundle.getString("time2");
                    Log.i(TAG, "onFragmentResult:  AddNotes"+date+".."+time+".."+date2+".."+time2);
                    // Do something with the result
                }}
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_notes, container, false);
        btnAddNote=view.findViewById(R.id.btn_addNote);
        btnDeleteNote=view.findViewById(R.id.btn_deleteNote);
        btnSaveNotes=view.findViewById(R.id.btn_saveNotes);
        recyclerView=view.findViewById(R.id.recyclerView);
        result = new Bundle();
        linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        if(AddTripActivity.key==3) {
            btnSaveNotes.setText("Edit");

            Log.i(TAG, "onCreateView: thread");
        }else {
            selectedNotes=new ArrayList<>();
            selectedNotes.add("");
            adapter=new AdapterAddNote(selectedNotes,getContext());
            recyclerView.setAdapter(adapter);

        }
        Log.i(TAG, "onCreateView: ");
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i(TAG, "onClick:add button "+selectedNotes.toString());
                if(selectedNotes.size()<=10){
                    selectedNotes.add("");

                    Log.i(TAG, selectedNotes.toString());
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(),"you can only add 10 notes",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedNotes.size()>0){
                    adapter.removeItem();
                    if(selectedNotes.size()==0)
                        btnDeleteNote.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        btnSaveNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notes=new ArrayList<>();
                if(AddTripActivity.key==1){
                    result = new Bundle();
                    if(!selectedNotes.isEmpty()) {
                        for (int i = 0; i < selectedNotes.size(); i++) {
                            if(!selectedNotes.get(i).isEmpty()&&selectedNotes.get(i)!=null) {
                                notes.add(selectedNotes.get(i));
                                //           result.putStringArrayList("bundleKey", selectedNotes);
                                Log.i(TAG, "onClick:Savebutton " + selectedNotes.get(i));
                            }
                        }
                        //    Log.i(TAG, "onClick: "+notes);
                        result.putStringArrayList("bundleKey", notes);
                    }


                }else if(AddTripActivity.key==3){
                    Log.i(TAG, "run: "+selectedNotes);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: "+selectedNotes);

                            getActivity().finish(); //added by amr
                            Log.i(TAG, "run: "+selectedNotes);
                        }
                    }).start();
                }
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                fm.popBackStack ("name", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        //edit in all methods
        //   result.putStringArrayList("bundleKey",selectedNotes);
        if(AddTripActivity.key==1){
            if(date!="")
                result.putString("date",date);
            if(time!="")
                result.putString("time",time);
            if(date2!="")
                result.putString("date2",date2);
            if(time2!="")
                result.putString("time2",time2);
            getParentFragmentManager().setFragmentResult("requestKey", result);
            Log.i(TAG, "onStop: "+result);
        }
    }


}