package com.example.remotejoystick17.view_model;

import android.os.Looper;
import android.util.Log;

import com.example.remotejoystick17.MsgUtil.MsgUtil;
import com.example.remotejoystick17.model.Model;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


public class ViewModel extends Observable implements Observer {

    private Model model;

    public ViewModel(Model model) {
        this.model = model;
    }

    public void setAileron(float a) {
        model.aileron(a);
    }

    public void setElevator(float e) {
        model.elevator(e);
    }

    public void setRudder(float r) {
        model.rudder(r);
    }

    public void setThrottle(float t) {
        model.throttle(t);
    }

    public void connectToFlightGear(String ip, String port) {
            int portNumber = Integer.parseInt(port);
            model.connectToFlightGear(ip, portNumber);
    }

    private void connectionFailed() {
        setChanged();
        notifyObservers(MsgUtil.CONNECTION_FAILED);
    }

    private void connectionSucceeded() {
        setChanged();
        notifyObservers(MsgUtil.CONNECTION_SUCCESS);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            if (model.isConnected()) {
                connectionSucceeded();
            } else {
                connectionFailed();
            }

        }
    }
}
