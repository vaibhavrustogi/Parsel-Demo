package com.example.vaibhavrustogi.parseldemo.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.vaibhavrustogi.parseldemo.R;

import login.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showLoginScreen();
    }

    private void showLoginScreen(){
        LoginFragment loginFragment = new LoginFragment();
        addToStack(loginFragment,false,R.id.fragment_container);
    }

    public void addToStack(Fragment fragment, boolean isAddToBackStack, int targetId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String tag = tag = fragment.getClass().getName();

        transaction.replace(targetId, fragment, tag);
        if (isAddToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }
}
