package com.nowakowski.krzysztof95.navigationdrawerapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddCommentFragment extends Fragment implements View.OnClickListener {

    private static final String url = "http://192.168.0.73:8888";

    @Digits(integer = 3)
    @Max(value = 1000, message = "Should be less than 1000")
    @NotEmpty
    private EditText book_idEditText;
    @NotEmpty
    private EditText authorEditText;
    @NotEmpty
    private EditText commentEditText;

    Validator validator = new Validator(this);


    public AddCommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_comment, container, false);


        Button send = (Button) v.findViewById(R.id.sendButton);
        send.setOnClickListener(this);
        book_idEditText = (EditText) v.findViewById(R.id.book_id);
        authorEditText = (EditText) v.findViewById(R.id.authorEditText);
        commentEditText = (EditText) v.findViewById(R.id.commentEditText);

        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                CreateNewCommentRequest(book_idEditText.getText().toString(), authorEditText.getText().toString(), commentEditText.getText().toString());
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(getContext());


                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return v;
    }

    private void CreateNewCommentRequest(String book_id, String author, String comment) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        final ListItem listItem = new ListItem();

        listItem.setBook_id(book_id);
        listItem.setAuthor(author);
        listItem.setComment(comment);

        Call<ListItem> call = service.sendComment(listItem);

        call.enqueue(new Callback<ListItem>() {
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                Toast.makeText(getContext(), "Send", Toast.LENGTH_LONG).show();
                book_idEditText.getText().clear();
                authorEditText.getText().clear();
                commentEditText.getText().clear();
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                Log.d("Log", "OnFailure" + t.getMessage());
            }
        });
    }


    @Override
    public void onClick(View view) {
        validator.validate();
    }
}
