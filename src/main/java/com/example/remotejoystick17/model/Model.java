package com.example.remotejoystick17.model;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Model extends Observable {

    private static String newline = "\r\n";

    private boolean stop;
    private static boolean isConnected = false;
    private Socket fg;
    private PrintWriter out;
    private Thread fgThread;
    private BlockingQueue<Runnable> fgCommands = new LinkedBlockingDeque<Runnable>();


    public Model() {
        fg = null;
//        pool = Executors.newFixedThreadPool(1);
    }

    public void aileron(float a) {
        //send to flight gear
    }

    public void elevator(float e) {
        //send to flight gear.
    }

    public void rudder(double r) {
        fgCommands.add(new Runnable() {
            @Override
            public void run() {
                out.print("set /controls/flight/rudder " + r + newline);
                out.flush();
            }
        });
    }

    public void throttle(double t) {
        fgCommands.add(new Runnable() {
            @Override
            public void run() {
                out.print("set /controls/engines/current-engine/throttle " + t + newline);
                out.flush();
            }
        });

    }



    public void connectToFlightGear(String ip, int port) {

        fgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //connecting to flightgear simulator.
                try {
                    fg = new Socket(ip, port);
                    stop = false;
                    out = new PrintWriter(fg.getOutputStream(), true);
                    isConnected = true;
                    synchronized (this) {
                        //notify if connection is succeeded or failed
                        setChanged();
                        notifyObservers(isConnected);
                    }
                } catch (IOException e) {
                    isConnected = false;
                    synchronized (this) {
                        //notify if connection is succeeded or failed
                        setChanged();
                        notifyObservers(isConnected);
                    }
                }

                while (!stop) {
                    try {
                        fgCommands.take().run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        fgThread.start();

    }

    public boolean isConnected() {
        return isConnected;
    }

}
