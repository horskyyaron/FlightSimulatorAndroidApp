package com.example.remotejoystick17.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.remotejoystick17.R;
import com.example.remotejoystick17.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creating data binding with the view.
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setContentView(R.layout.activity_main);
    }
}