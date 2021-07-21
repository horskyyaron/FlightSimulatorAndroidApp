package com.example.remotejoystick17.model;


import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

public class Model extends Observable {

    private static boolean isConnected = false;
    private Thread t;
    private Socket fg;

    public Model() {
        t =  null;
        fg = null;
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

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fg = new Socket(ip,port);
                    isConnected = true;
                } catch (IOException e) {
                    isConnected = false;
                } finally {

                    synchronized (this) {
                        setChanged();
                        notifyObservers(isConnected);
                    }
                }
            }
        });
        t.start();

    }

    public boolean isConnected() {
        return isConnected;
    }

}
