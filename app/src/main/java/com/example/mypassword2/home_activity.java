package com.example.mypassword2;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class home_activity extends AppCompatActivity implements LifecycleObserver {

    private final static int addAccountRequestCode = 1;
    private final static String accountsListTitle = "Accounts";
    private final static String addFormTitle = "Add a Password";
    private final static String activityListTitle = "Activity";
    private PassWordORM passWordORM;
    private ProgressBar progressBar;
    AccountListFragment accountListFragment;
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == addAccountRequestCode){
                String user = data.getStringExtra("user");
                String pw = data.getStringExtra("pw");
                String med = data.getStringExtra("med");
                // test add
                if(passWordORM.insert(med, user, pw)){
                    Toast.makeText(this, "Added Successfully!", Toast.LENGTH_SHORT);
                    mediumsList.add(med);
                    mediumsListAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
                }


            }
        }
    }
*/

    private class DownloadDataBase extends AsyncTask<Void, Void, Void>{

        Context context;
        @Override
        protected Void doInBackground(Void... v){
            passWordORM = new PassWordORM(context);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... v){
            progressBar.setIndeterminate(true);
        }

        @Override
        protected void onPostExecute(Void v){
            startExec();
        }


        public void setContext(Context context){
            this.context = context;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        setContentView(R.layout.activity_home_activity);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        DownloadDataBase downloadDataBase = new DownloadDataBase();
        downloadDataBase.setContext(this);
        downloadDataBase.execute();


        getSupportActionBar().setTitle(accountsListTitle);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2e2d2d")));

    }

    private void startExec(){
        accountListFragment = new AccountListFragment();
        loadFragment(accountListFragment);

        BottomNavigationView bottomNavigationView =  (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){

                    case R.id.bottombaritem_accounts:
                        loadFragment(accountListFragment);
                        getSupportActionBar().setTitle(accountsListTitle);
                        return true;
                    case R.id.bottombaritem_add:
                        loadFragment(new AddAccountFormFragment());
                        getSupportActionBar().setTitle(addFormTitle);
                        return true;
                    case R.id.bottombaritem_usage:
                        loadFragment(new AccountUsageFragment());
                        getSupportActionBar().setTitle(activityListTitle);
                        return true;
                    default:
                        return false;
                }

            }
        });

        progressBar.setVisibility(View.GONE);
    }

    private void loadFragment(Fragment fragment){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public PassWordORM getPassWordORM(){
        return passWordORM;
    }

    @Override
    public void onBackPressed(){
        return;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackground(){
        finish();
    }




}