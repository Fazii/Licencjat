package com.nowakowski.krzysztof95.navigationdrawerapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;


public class YourEventsFragment extends Fragment {

    private final String url = "http://192.168.0.73:8888";


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private FrameLayout emptyView;
    private List<ListItem> responseItems;
    View v;

    public YourEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_your_events, container, false);
        final SharedPreferences prefs = getApplicationContext().getSharedPreferences("Name", Context.MODE_PRIVATE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshLayoutYourEvents);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecycleViewData(prefs.getString("user_id", ""));
            }
        });
        emptyView = (FrameLayout) v.findViewById(R.id.empty_view_your_events);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleViewYourEvents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        responseItems = new ArrayList<>();
        loadRecycleViewData(prefs.getString("user_id", ""));

        return v;
    }


    private void loadRecycleViewData(String user_id) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.Loading_data));
        progressDialog.show();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        ListItem requestItems = new ListItem();
        requestItems.setUser_id(user_id);
        Call<List<ListItem>> call = service.getMyEvents(requestItems);

        call.enqueue(new Callback<List<ListItem>>() {
            @Override
            public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                responseItems = response.body();

                adapter = new MyAdapter(responseItems, getContext());

                if (adapter.getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<ListItem>> call, Throwable t) {
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }
}
