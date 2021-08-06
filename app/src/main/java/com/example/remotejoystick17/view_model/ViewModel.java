package com.example.remotejoystick17.view_model;

import com.example.remotejoystick17.MsgUtil.Util;
import com.example.remotejoystick17.model.Model;

import java.util.Observable;
import java.util.Observer;


public class ViewModel extends Observable implements Observer {


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
            portNumber = Util.NOT_VALID_PORT;
        }
        if (portNumber == Util.NOT_VALID_PORT) {
            setChanged();
            notifyObservers(Util.NOT_VALID_PORT_INPUT_MSG);
            return;
        }

        model.connectToFlightGear(ip, portNumber);
    }

    private void connectionFailed() {
        setChanged();
        notifyObservers(Util.CONNECTION_FAILED_MSG);
    }

    private void connectionSucceeded() {
        setChanged();
        notifyObservers(Util.CONNECTION_SUCCESS_MSG);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            if (model.isConnected()) {
                connectionSucceeded();
            } else {
                if (arg.equals(Util.DISCONNECTED)) {
                    connectionDisconnected();
                } else {
                    connectionFailed();
                }
            }

        }
    }

    private void connectionDisconnected() {
        setChanged();
        notifyObservers(Util.CONNECTION_DISCONNECTED_MSG);
    }

    public void disconnectFromFlightGear() {
        model.disconnect();
    }
}
