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

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView ;
    HistoryRecyclerAdapter historyRecyclerAdapter;
    List<ModelHistory> historyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view    = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_History);
        historyList = createListForAdapter();
        historyRecyclerAdapter = new HistoryRecyclerAdapter(historyList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(historyRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext(),RecyclerView.VERTICAL , false));

        return  view ;
    }
    private List<ModelHistory> createListForAdapter()
    {
        List <ModelHistory> lists = new ArrayList<>();
        for (int i = 0 ; i<10 ; i++)
        {
            lists.add(new ModelHistory("Monday",
                    "Family Trip " ,
                    "Cairo" ,
                    "Giza"));
        }
        return lists;
    }
}
