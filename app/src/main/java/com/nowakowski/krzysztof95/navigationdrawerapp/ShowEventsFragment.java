package com.nowakowski.krzysztof95.navigationdrawerapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.nowakowski.krzysztof95.navigationdrawerapp.ConfigurationConstants.API_URL;
import static com.nowakowski.krzysztof95.navigationdrawerapp.ConfigurationConstants.SHOW_EVENTS_CLASS;


public class ShowEventsFragment extends Fragment {


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private FrameLayout emptyView;
    public List<ListItem> listItems;


    public ShowEventsFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_show_events, container, false);


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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String query = bundle.getString("query", "");
            loadFilteredRecycleViewData(query);
        }
        else loadRecycleViewData();

        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);

        return v;
    }


    private void loadFilteredRecycleViewData(String tag) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.Loading_data));
        progressDialog.show();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        ListItem requestItems = new ListItem();
        requestItems.setEvent_tag(tag);

        Call<List<ListItem>> call = service.getEventsDetailsTags(requestItems);

        call.enqueue(new Callback<List<ListItem>>() {

            @Override
            public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

                try {
                    listItems.clear();
                    listItems = response.body();

                    mSwipeRefreshLayout.setRefreshing(false);
                    progressDialog.dismiss();

                    adapter = new MyAdapter(listItems, getContext(), SHOW_EVENTS_CLASS);

                    recyclerView.setAdapter(adapter);

                    if (adapter.getItemCount() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }

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
                Toast.makeText(getContext(), "Błąd połączenia", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadRecycleViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.Loading_data));
        progressDialog.show();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        Call<List<ListItem>> call = service.getEventDetails();

        call.enqueue(new Callback<List<ListItem>>() {

            @Override
            public void onResponse(Call<List<ListItem>> call, Response<List<ListItem>> response) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

                try {
                    listItems.clear();
                    listItems = response.body();

                    mSwipeRefreshLayout.setRefreshing(false);
                    progressDialog.dismiss();

                    adapter = new MyAdapter(listItems, getContext(), SHOW_EVENTS_CLASS);

                    recyclerView.setAdapter(adapter);

                    if (adapter.getItemCount() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }

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
