package com.example.tirhal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    RecyclerView recyclerView;
    ImageView emptyListImg;
    HistoryRecyclerAdapter historyRecyclerAdapter;
    int finishedTripsNum;
    //    List<ModelHistory> historyList;
    private List tripsList = new ArrayList<Trip>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        new LoadRoomData().execute();
        recyclerView = view.findViewById(R.id.recycler_view_History);
//       historyList = createListForAdapter();

        historyRecyclerAdapter = new HistoryRecyclerAdapter(getContext(), tripsList, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(historyRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext(), RecyclerView.VERTICAL, false));

        return view;
    }

    private class LoadRoomData extends AsyncTask<Void, Void, List<Trip>> {

        @Override
        protected List<Trip> doInBackground(Void... voids) {
            finishedTripsNum = FragmentMainActivity.database.tripDAO().getCountTripType(FragmentMainActivity.fireBaseUseerId, "finished");
            return FragmentMainActivity.database.tripDAO().selectHistoryTrip(FragmentMainActivity.fireBaseUseerId, "cancelled", "finished", "missed");
        }

        @Override
        protected void onPostExecute(List<Trip> trips) {
            super.onPostExecute(trips);
            tripsList = trips;

            historyRecyclerAdapter = new HistoryRecyclerAdapter(getContext(), tripsList, getActivity());
            recyclerView.setAdapter(historyRecyclerAdapter);
        }
    }
}

