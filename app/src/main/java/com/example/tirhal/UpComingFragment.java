package com.example.tirhal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UpComingFragment extends Fragment {
    RecyclerView recyclerView ;
    UpcomingRecyclerAdapter UpComingRecyclerAdapter;
    List<ModelHistory> UpcomingList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState ){
        View view =  inflater.inflate(R.layout.fragment_upcoming, container,false);
        recyclerView = view.findViewById(R.id.recycler_view_Upcoming);
        UpcomingList = createListForAdapter();
        UpComingRecyclerAdapter = new UpcomingRecyclerAdapter(UpcomingList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(UpComingRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext(),RecyclerView.VERTICAL , false));


        return  view ;
    }
    private List<ModelHistory> createListForAdapter()
    {
        List <ModelHistory> lists = new ArrayList<>();
        for (int i = 0 ; i<10 ; i++)
        {
            lists.add(new ModelHistory("Friday",
                    "Family Trip " ,
                    "Cairo" ,
                    "Giza"));
        }
        return lists;
    }
}
