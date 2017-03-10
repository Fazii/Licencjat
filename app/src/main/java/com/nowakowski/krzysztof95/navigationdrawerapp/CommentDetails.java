package com.nowakowski.krzysztof95.navigationdrawerapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.security.AccessController.getContext;

public class CommentDetails extends AppCompatActivity {
    private static final String url = "http://192.168.0.73:8888";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_details);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView textViewId = (TextView) findViewById(R.id.d_textVievId);
        TextView textViewBookId = (TextView) findViewById(R.id.d_textVievBookId);
        TextView textViewAuthor = (TextView) findViewById(R.id.d_textVievAuthor);
        TextView textViewCommentDate = (TextView) findViewById(R.id.d_textVievCommentDate);
        TextView textViewBookComment = (TextView) findViewById(R.id.d_textVievBookComment);

        textViewId.setText(String.format("Id: %s", getIntent().getStringExtra("id")));
        textViewBookId.setText(String.format("Book Id: %s", getIntent().getStringExtra("book_id")));
        textViewAuthor.setText(String.format("Author: %s", getIntent().getStringExtra("author")));
        textViewCommentDate.setText(String.format("Comment date: %s", getIntent().getStringExtra("comment_date")));
        textViewBookComment.setText(String.format("Comment: %s", getIntent().getStringExtra("book_comment")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void DeleteCommentRequest(String id) {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        final ListItem listItem = new ListItem();

        listItem.setId(id);

        Call<ListItem> call = service.DeleteComment(listItem);

        call.enqueue(new Callback<ListItem>() {
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                Toast.makeText(CommentDetails.this, "Comment Deleted", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
                Toast.makeText(CommentDetails.this, "Error", Toast.LENGTH_LONG).show();
                Log.d("Log", "OnFailure" + t.getMessage());
            }
        });
    }

    public void onDeleteCommentClick(View view) {
        String id = getIntent().getStringExtra("id");
        DeleteCommentRequest(id);
    }
}
