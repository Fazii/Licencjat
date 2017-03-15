package com.nowakowski.krzysztof95.navigationdrawerapp;


import android.app.ProgressDialog;
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


public class ShowCommentsFragment extends Fragment {

    private static final String url = "http://192.168.0.73:8888";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private FrameLayout emptyView;
    private List<ListItem> listItems;


    public ShowCommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_show_comments, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecycleViewData();
            }
        });
        emptyView = (FrameLayout) v.findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listItems = new ArrayList<>();

        loadRecycleViewData();

        return v;
    }

    private void loadRecycleViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.Loading_data));
        progressDialog.show();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        Call<List<ListItem>> call = service.getBookDetails();

        call.enqueue(new Callback<List<ListItem>>() {

            @Override
            public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
//              TODO add empty list support
                try {
                    listItems.clear();
                    listItems = response.body();

                    mSwipeRefreshLayout.setRefreshing(false);
                    progressDialog.dismiss();

                    adapter = new MyAdapter(listItems, getContext());

                    recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ListItem>> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }
}
