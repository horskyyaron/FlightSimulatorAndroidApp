package com.example.remotejoystick17.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.graphics.Color;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.remotejoystick17.MsgUtil.MsgUtil;
import com.example.remotejoystick17.R;
import com.example.remotejoystick17.databinding.ActivityMainBinding;
import com.example.remotejoystick17.model.Model;
import com.example.remotejoystick17.view_model.ViewModel;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements Observer {


    private ViewModel vm;
    private Model m;
    Context context;
    private ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creating data binding with the view.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        context = getApplicationContext();
        m = new Model();
        vm = new ViewModel(m);

        m.addObserver(vm);
        vm.addObserver(this);


        binding.connectButton.setOnClickListener(v -> {
            binding.connectButton.setText("connecting...");
            vm.connectToFlightGear(binding.ipEditText.getText().toString(), binding.portEditText.getText().toString());

        });

    }


    @Override
    public void update(Observable o, Object arg) {
        Toast toast;
        if(o == vm) {
            if(arg.equals(MsgUtil.CONNECTION_FAILED)) {
                Looper.prepare();
                toast = Toast.makeText(context, MsgUtil.CONNECTION_FAILED, Toast.LENGTH_SHORT);
                toast.show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.connectButton.setText("CONNECT");
                    }
                });
                Looper.loop();

            }
            if(arg.equals(MsgUtil.CONNECTION_SUCCESS)) {
                Looper.prepare();
                toast = Toast.makeText(context, MsgUtil.CONNECTION_SUCCESS, Toast.LENGTH_SHORT);
                toast.show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.connectButton.setText("CONNECTED!");
                        binding.connectButton.setBackgroundColor(Color.GREEN);
                        binding.connectButton.setEnabled(false);
                        binding.connectButton.setTextColor(Color.DKGRAY);
                    }
                });
                Looper.loop();

            }
        }
    }
}