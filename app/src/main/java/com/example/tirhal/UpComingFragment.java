package com.example.tirhal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UpComingFragment extends Fragment {
    RecyclerView recyclerView ;
    UpcomingRecyclerAdapter UpComingRecyclerAdapter;
//    List<ModelHistory> UpcomingList;
      private List tripsList = new ArrayList<Trip>();
    FloatingActionButton floatingBtnAdd;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new LoadRoomData().execute();
        floatingBtnAdd = view.findViewById(R.id.btn_floating);
        initializeAddTrip();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState ){
        View view =  inflater.inflate(R.layout.fragment_upcoming, container,false);
        recyclerView = view.findViewById(R.id.recycler_view_Upcoming);
//        UpcomingList = createListForAdapter();
        UpComingRecyclerAdapter = new UpcomingRecyclerAdapter(getContext(),tripsList,getActivity());
        recyclerView.setHasFixedSize(true);
       recyclerView.setAdapter(UpComingRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext(),RecyclerView.VERTICAL , false));


        return  view ;
    }
//    private List<ModelHistory> createListForAdapter()
//    {
//        List <ModelHistory> lists = new ArrayList<>();
//        for (int i = 0 ; i<10 ; i++)
//        {
//            lists.add(new ModelHistory("Friday",
//                    "Family Trip " ,
//                    "Cairo" ,
//                    "Giza"));
//        }
//        return lists;
//    }
    public void initializeAddTrip() {
        floatingBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddTripActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("KEY", 1);
                bundle.putInt("ID", -1);
                intent.putExtras(bundle);

                //   intent.putExtra("KEY",1);
                // intent.putExtra("ID",-1);
                startActivity(intent);
            }
        });
    }
    private class LoadRoomData extends AsyncTask<Void, Void, List<Trip>> {

        @Override
        protected List<Trip> doInBackground(Void... voids) {
            return FragmentMainActivity.database.tripDAO().selectUpcomingTrip(FragmentMainActivity.fireBaseUseerId, "upcoming");
        }

        @Override
        protected void onPostExecute(List<Trip> trips) {
            super.onPostExecute(trips);
            tripsList = trips;
            if (tripsList.isEmpty()) {

            } else {

            }
            UpComingRecyclerAdapter = new UpcomingRecyclerAdapter(getContext(), tripsList,getActivity());
            recyclerView.setAdapter(UpComingRecyclerAdapter);
        }
    }

}
