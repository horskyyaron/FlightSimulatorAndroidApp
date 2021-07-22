package com.example.remotejoystick17.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.remotejoystick17.MsgUtil.MsgUtil;
import com.example.remotejoystick17.R;
import com.example.remotejoystick17.databinding.ActivityMainBinding;
import com.example.remotejoystick17.model.Model;
import com.example.remotejoystick17.view_model.ViewModel;

import java.util.Observable;
import java.util.Observer;

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


        //connect button click event
        binding.connectButton.setOnClickListener((View view) -> {

            // Hides the keyboard.
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            //updating text on button and make it unclickable during the connection attempt
            binding.connectButton.setText(MsgUtil.CONNECTING);
            binding.connectButton.setEnabled(false);

            //try to connect.
            vm.connectToFlightGear(binding.ipEditText.getText().toString(), binding.portEditText.getText().toString());
        });

        binding.rudderSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                double value = (float) progress / 50.0 - 1;
                vm.setRudder(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.throttleVerticalSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double value = (float) progress / 100;
                vm.setThrottle(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.joystick.setOnJoystickChangeListener(new Joystick.OnJoystickChangeListener() {
            @Override
            public void onChange(double x, double y) {
                vm.setAileron(x);
                vm.setElevator(y);
            }
        });



    }


    @Override
    public void update(Observable o, Object arg) {
        Toast toast;
        if (o == vm) {
            if (arg.equals(MsgUtil.CONNECTION_FAILED)) {
                showConnectionToast(MsgUtil.CONNECTION_FAILED, MsgUtil.CONNECT, true, Color.CYAN);
            }
            if (arg.equals(MsgUtil.CONNECTION_SUCCESS)) {
                showConnectionToast(MsgUtil.CONNECTION_SUCCESS, MsgUtil.CONNECTED, false, Color.GREEN);

            }
        }
    }

    private void showConnectionToast(String toastMsg, String endingBtnTxt, boolean isBtnClickableAtEnd, int endingBtnColor) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.connectButton.setText(endingBtnTxt);
                binding.connectButton.setBackgroundColor(endingBtnColor);
                binding.connectButton.setEnabled(isBtnClickableAtEnd);
                binding.connectButton.setTextColor(Color.BLACK);

            }
        });
    }
}