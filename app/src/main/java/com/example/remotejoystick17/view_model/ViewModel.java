package com.example.remotejoystick17.view_model;

import android.util.Log;

import com.example.remotejoystick17.MsgUtil.MsgUtil;
import com.example.remotejoystick17.model.Model;

import java.util.Observable;
import java.util.Observer;


public class ViewModel extends Observable implements Observer {

    private static final int NOT_VALID_PORT = -1;
    private Model model;

    public ViewModel(Model model) {
        this.model = model;
    }

    public void setAileron(double a) {
        model.aileron(a);
    }

    public void setElevator(double e) {
        model.elevator(e);
    }

    public void setRudder(double r) {
        model.rudder(r);
    }

    public void setThrottle(double t) {
        model.throttle(t);
    }

    public boolean isConnected() {
        return model.isConnected();
    }

    public void connectToFlightGear(String ip, String port) {
        //for initiallizing
        int portNumber = 0;
        try {
            portNumber = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            portNumber = NOT_VALID_PORT;
        }
        if (portNumber == NOT_VALID_PORT) {
            setChanged();
            notifyObservers(MsgUtil.NOT_VALID_PORT_INPUT_MSG);
            return;
        }

        model.connectToFlightGear(ip, portNumber);
    }

    private void connectionFailed() {
        setChanged();
        notifyObservers(MsgUtil.CONNECTION_FAILED_MSG);
    }

    private void connectionSucceeded() {
        setChanged();
        notifyObservers(MsgUtil.CONNECTION_SUCCESS_MSG);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            if (model.isConnected()) {
                connectionSucceeded();
            } else {
                if (arg.equals(MsgUtil.DISCONNECTED)) {
                    connectionDisconnected();
                } else {
                    connectionFailed();
                }
            }

        }
    }

    private void connectionDisconnected() {
        setChanged();
        notifyObservers(MsgUtil.CONNECTION_DISCONNECTED_MSG);
    }

    public void disconnectFromFlightGear() {
        model.disconnect();
    }
}
