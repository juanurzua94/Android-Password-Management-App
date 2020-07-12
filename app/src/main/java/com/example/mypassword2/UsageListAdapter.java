package com.example.mypassword2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class UsageListAdapter extends ArrayAdapter {

    private Activity context;
    private HashMap<String, ArrayList<String>> usageTable;
    private ArrayList<String> usageList;

    public UsageListAdapter(Activity context, HashMap<String, ArrayList<String>> usageTable){
        super(context, R.layout.usage_list_view, usageTable.keySet().toArray());
        this.context = context;
        this.usageTable = usageTable;
        this.usageList = new ArrayList<>();
        usageList.addAll(usageTable.keySet());
        Collections.sort(usageList, Collections.<String>reverseOrder());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.usage_list_view, null, true);

        if(usageList.size() > 0) {
            TextView mediumTextView = rowView.findViewById(R.id.mediumView);
            TextView userEmailView = rowView.findViewById(R.id.userOrEmailView);
            TextView dateView = rowView.findViewById(R.id.dateView);


            mediumTextView.setText(usageTable.get(usageList.get(position)).get(0));
            userEmailView.setText(usageTable.get(usageList.get(position)).get(1));

            String[] dateTime = usageList.get(position).split(" ");

            dateView.setText(dateTime[0]);


        }

        return rowView;
    }

    @Override
    public void clear(){
        usageList.clear();
        usageTable.clear();
    }
}
