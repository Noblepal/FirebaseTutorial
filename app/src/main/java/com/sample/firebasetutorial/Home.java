package com.sample.firebasetutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.sample.firebasetutorial.fragments.LandLord;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadFragment(new LandLord());
    }

    private void loadFragment(Fragment activeFragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, activeFragment).addToBackStack(null).commit();
    }

}