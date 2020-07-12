package com.example.mypassword2;

import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AccountListAdapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> mainTitle;

    public AccountListAdapter(Activity context, ArrayList<String> mainTitle){
        super(context, R.layout.accounts_list_view, mainTitle);

        this.context = context;
        this.mainTitle = mainTitle;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inlfater = context.getLayoutInflater();
        View rowView = inlfater.inflate(R.layout.accounts_list_view, null, true);

        TextView accoountTextView = rowView.findViewById(R.id.accountTextView);

        accoountTextView.setText(mainTitle.get(position));
        return rowView;
    }
}
