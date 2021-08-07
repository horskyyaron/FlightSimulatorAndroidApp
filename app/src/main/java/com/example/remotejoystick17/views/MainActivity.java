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

import com.example.remotejoystick17.MsgUtil.Util;
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

        binding.connectButton.setBackgroundColor(Util.DEFUALT_CONNECT_BUTTON_COLOR);
        binding.disconnectBtn.setVisibility(View.INVISIBLE);
        binding.disconnectBtn.setEnabled(false);

        //connect button click event
        binding.connectButton.setOnClickListener((View view) -> {

            // Hides the keyboard.
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            //updating text on button and make it unclickable during the connection attempt
            binding.connectButton.setText(Util.CONNECTING);
            binding.connectButton.setEnabled(false);

            //try to connect.
            vm.connectToFlightGear(binding.ipEditText.getText().toString(), binding.portEditText.getText().toString());
        });

        binding.disconnectBtn.setOnClickListener((View view) -> {
            vm.disconnectFromFlightGear();
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
                if (vm.isConnected()) {
                    vm.setAileron(x);
                    vm.setElevator(y);
                }
            }
        });

    }


    @Override
    public void update(Observable o, Object arg) {

        if (o == vm) {
            switch (arg.toString()) {
                case Util.CONNECTION_SUCCESS_MSG:
                    updateDisconnectBtn(View.VISIBLE, true);
                    showConnectionToast(Util.CONNECTION_SUCCESS_MSG, Util.CONNECTED, false, Color.GREEN);
                    break;
                case Util.CONNECTION_FAILED_MSG:
                    showConnectionToast(Util.CONNECTION_FAILED_MSG, Util.CONNECT, true, Util.DEFUALT_CONNECT_BUTTON_COLOR);
                    break;
                case Util.CONNECTION_DISCONNECTED_MSG:
                    updateDisconnectBtn(View.INVISIBLE, false);
                    showConnectionToast(Util.CONNECTION_DISCONNECTED_MSG, Util.CONNECT, true, Util.DEFUALT_CONNECT_BUTTON_COLOR);
                    break;
                case Util.NOT_VALID_PORT_INPUT_MSG:
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //updating text on button and make it unclickable during the connection attempt
                            binding.connectButton.setText(Util.CONNECT);
                            binding.connectButton.setEnabled(true);
                            Toast toast = Toast.makeText(context, Util.NOT_VALID_PORT_INPUT_MSG,Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                    break;
                default:
                    return;
            }
        }
    }

    private void updateDisconnectBtn(int visibility, boolean isEnabled) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.disconnectBtn.setVisibility(visibility);
                binding.disconnectBtn.setEnabled(isEnabled);
            }
        });
    }

    private void showConnectionToast(String toastMsg, String connectBtnEndingText, boolean isConnectBtnEnabled, int connectBtnColor) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.connectButton.setText(connectBtnEndingText);
                binding.connectButton.setBackgroundColor(connectBtnColor);
                binding.connectButton.setEnabled(isConnectBtnEnabled);
                binding.connectButton.setTextColor(Color.BLACK);
                Toast toast = Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}