package com.example.mypassword2;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Observable;


public class AccountUsageFragment extends Fragment {
    View view;
    private home_activity homePassWordORM;

    UsageListAdapter usageListAdapter;
    ListView usageListView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.usage_menu_display, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.clear_usage:
                clearUsageDialog();
                return true;
            default:
                return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        view = inflater.inflate(R.layout.usage_list_fragment, container, false);
        homePassWordORM = (home_activity) getActivity();
        usageListAdapter = new UsageListAdapter(getActivity(), homePassWordORM.getPassWordORM().getUsageInfo());
        usageListView = view.findViewById(R.id.usageList);
        usageListView.setAdapter(usageListAdapter);


        return view;
    }

    private void clearUsageDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure you want to delete all data usage?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUsage();
                    }
                })
                .setNegativeButton("Cancel", null).show();
    }

    private void clearUsage(){
        if(homePassWordORM.getPassWordORM().clearAccessEntries()){
            usageListAdapter.clear();
            usageListAdapter = new UsageListAdapter(getActivity(), homePassWordORM.getPassWordORM().getUsageInfo());
            usageListView.setAdapter(usageListAdapter);
        }
    }


}
