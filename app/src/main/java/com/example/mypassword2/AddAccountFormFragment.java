package com.example.mypassword2;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class AddAccountFormFragment extends Fragment {
    View view;

    private Button submitButton;
    private EditText medium;
    private EditText user;
    private EditText password;
    private home_activity homePassWordORM;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        homePassWordORM = (home_activity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){

        view = inflater.inflate(R.layout.add_account_fragment, container, false);

        submitButton = (Button) view.findViewById(R.id.submitButton);
        medium = (EditText) view.findViewById(R.id.mediumName);
        user = (EditText) view.findViewById(R.id.userNameOrEmail);
        password = (EditText) view.findViewById(R.id.Password);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        return view;
    }

    private void getData(){

        String mediumInput = medium.getText().toString();
        String userInput = user.getText().toString();
        String passwordInput = password.getText().toString();

        Log.i("AddAccountFormFrag", mediumInput);
        // success message
        if(homePassWordORM.getPassWordORM().insert(mediumInput, userInput, passwordInput)){
            Toast.makeText(getContext(), "Successfully saved!", Toast.LENGTH_SHORT).show();
        }
        // error message
        else {
            Toast.makeText(getContext(), "ERROR!", Toast.LENGTH_SHORT).show();
        }
    }
}
