package com.example.mypassword2;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.zip.Inflater;

public class AccountListFragment extends Fragment {

    View view;
    TextView header;
    private ListView mediumsListView;
    private ArrayList<String> mediumsList;
    private AccountListAdapter mediumsListAdapter;

    private ArrayList<String> returnedUsers;
    private ArrayList<String> associatedAccounts;
    private ArrayAdapter<String> associatedAccountsAdapter;
    private String currentMedium = "";
    private home_activity homePassWordORM;

    private boolean displayingMediums;
    private boolean displayingAccounts;
    private boolean displayingPassword;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        homePassWordORM =  (home_activity) getActivity();
        mediumsList = homePassWordORM.getPassWordORM().allMediums();
        if(mediumsList == null)
            mediumsList = new ArrayList<>();
        mediumsListAdapter = new AccountListAdapter(getActivity(), mediumsList);

        associatedAccounts = null;
        associatedAccountsAdapter = null;

        displayingMediums = true;
        displayingAccounts = false;
        displayingPassword = false;

        /*
        try {
            mediumsList.addAll(passWordORM.allMediums());
            mediumsListAdapter.notifyDataSetChanged();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        */



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
                clearAllAccountDialog();
                return true;
            default:
                return false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
            view = inflater.inflate(R.layout.account_list_fragment, container, false);
            mediumsListView = view.findViewById(R.id.mediumsList);
            mediumsListView.setAdapter(mediumsListAdapter);
            setMediumsListListener();
            view.setFocusableInTouchMode(true);
            view.requestFocus();
            view.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    Log.i("STATUS", "INCORRECT KEY EVENT CONDITION");
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        Log.i("STATUS:", "WORKING");

                        if(displayingMediums && !displayingAccounts && !displayingPassword){
                            return true;
                        }

                        if(!displayingMediums && displayingAccounts && !displayingPassword){
                            goBackToMediums();
                        }

                    }
                    return false;
                }
            });

            return view;

    }

    // displays all users of a medium
    private void displayAccounts(String medium){
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an account from " + medium);

        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!currentMedium.equals("")){
                    currentMedium = "";
                }

                dialog.dismiss();
            }
        });

        returnedUsers = passWordORM.allUsersForMedium(medium);



        currentMedium = medium;

        String[] userNamesArray = new String[returnedUsers.size()];
        returnedUsers.toArray(userNamesArray);

        builder.setItems(userNamesArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String getUser = returnedUsers.get(which);
                showAccountInfo(getUser, dialog);

            }
        });

        AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
        */

        Log.i("Medium", medium);
        associatedAccounts = homePassWordORM.getPassWordORM().allUsersForMedium(medium);
        currentMedium = medium;
        associatedAccountsAdapter= new AccountListAdapter(getActivity(), associatedAccounts);
        mediumsListView.setAdapter(associatedAccountsAdapter);
        setAccountsListListener();
        displayingAccounts = true;
        displayingMediums = false;

    }

    // displays passsword of the 'user' account
    private void showAccountInfo(String user){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.show_password, null);

        TextView mediumView = (TextView) mView.findViewById(R.id.mediumView);

        TextView userNameView = (TextView) mView.findViewById(R.id.userNameView);
        TextView passwordView = (TextView) mView.findViewById(R.id.passwordView);

        Button dismiss = (Button) mView.findViewById(R.id.exitButton);

        mediumView.setText(currentMedium);
        userNameView.setText(user);
        passwordView.setText(homePassWordORM.getPassWordORM().getPw(currentMedium, user));


        builder.setView(mView);
        final AlertDialog newDialog = builder.create();
        newDialog.setCanceledOnTouchOutside(false);


        dismiss.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                newDialog.dismiss();
            }
        });

        newDialog.show();


    }

    private void clearAllAccountDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure you want to delete all accounts?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearAccounts();
                    }
                })
                .setNegativeButton("Cancel", null).show();
    }

    private void clearAccounts(){
        if(homePassWordORM.getPassWordORM().clearAllAccounts()){
            mediumsList.clear();
            mediumsListAdapter.notifyDataSetChanged();
        }
    }

    private void showAccountDeleteDialogue(final int position){
        new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure you want to delete this account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String med = associatedAccounts.get(position);
                        homePassWordORM.getPassWordORM().deleteEntry(currentMedium, med);
                        associatedAccounts.remove(position);
                        associatedAccountsAdapter.notifyDataSetChanged();
                        if(associatedAccounts.size() == 0) {
                            mediumsList.remove(currentMedium);
                            mediumsListAdapter.notifyDataSetChanged();
                            goBackToMediums();
                        }
                    }
                })
                .setNegativeButton("No", null).show();

    }

    private void goBackToMediums(){
        mediumsListView.setAdapter(mediumsListAdapter);
        displayingMediums = true;
        displayingAccounts = false;
        currentMedium = "";
        setMediumsListListener();
    }

    private void setMediumsListListener(){
        mediumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("displayAc", mediumsList.get(position));
                displayAccounts(mediumsList.get(position));
            }
        });

    }

    private void setAccountsListListener(){
        mediumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               showAccountInfo(associatedAccounts.get(position));
            }
        });

        mediumsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showAccountDeleteDialogue(position);
                return true;
            }
        });
    }


}
