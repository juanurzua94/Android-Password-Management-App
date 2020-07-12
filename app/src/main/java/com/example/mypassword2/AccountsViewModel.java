package com.example.mypassword2;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.mypassword2.PassWordORM;

import java.util.ArrayList;
import java.util.HashMap;

public class AccountsViewModel extends ViewModel {

    private final MutableLiveData<String> addedAccount = new MutableLiveData<String>();
    private final MutableLiveData<String> usageTime = new MutableLiveData<>();

    public MutableLiveData<String> getNewAccount(){
        return addedAccount;
    }

    public void setNewAccount(String accountName){
        addedAccount.setValue(accountName);
    }

    public MutableLiveData<String> getNewUsage(){ return usageTime ;}

    public void setNewUsage(String usage){
        usageTime.setValue(usage);
    }



}
