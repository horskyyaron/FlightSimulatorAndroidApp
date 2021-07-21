package com.example.remotejoystick17.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;

import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.remotejoystick17.R;
import com.example.remotejoystick17.databinding.ActivityMainBinding;
import com.example.remotejoystick17.model.Model;
import com.example.remotejoystick17.view_model.ViewModel;

public class MainActivity extends AppCompatActivity {

    //debugging stuff
    private static final String TAG = "MyActivity";

    private ViewModel vm;
    private Model m;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creating data binding with the view.
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        m = new Model();
        vm = new ViewModel(m);
        m.addObserver(vm);

        binding.connectButton.setOnClickListener(v -> {
            vm.connectToFlightGear(binding.ipEditText.getText().toString(), binding.portEditText.getText().toString());
        });

    }


}