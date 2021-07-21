package com.example.remotejoystick17.model;


import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

public class Model extends Observable {

    private static boolean isConnected = false;

    public Model() {
        //connect to flight gear?
    }

    public void aileron(float a) {
        //send to flight gear
    }

    public void elevator(float e) {
        //send to flight gear.
    }

    public void rudder(float r) {
        //send to flight gear.
    }

    public void throttle(float t) {
        //send to flight gear.
    }

    public void connectToFlightGear(String ip, int port) {
        //1 - connection success
        //else - connection fail.
        if(port == 1) {
            isConnected = true;
        } else {
            isConnected = false;
        }
        setChanged();
        notifyObservers();
    }

    public boolean isConnected() {
        return isConnected;
    }

}
