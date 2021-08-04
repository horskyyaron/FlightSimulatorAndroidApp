package com.example.remotejoystick17.model;


import android.util.Log;

import com.example.remotejoystick17.MsgUtil.MsgUtil;

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

    public void aileron(double a) {
        fgCommands.add(new Runnable() {
            @Override
            public void run() {
                out.print("set /controls/flight/aileron " + a + newline);
                out.flush();
            }
        });
    }

    public void elevator(double e) {
        fgCommands.add(new Runnable() {
            @Override
            public void run() {
                out.print("set /controls/flight/elevato r" + e + newline);
                out.flush();
            }
        });
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

                    while (isConnected) {
                        synchronized (this) {
                            //notify if connection is succeeded or failed
                            setChanged();
                            notifyObservers(isConnected);
                        }

                        while (!stop) {
                            try {
                                fgCommands.take().run();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    //closed connection.
                    fg.close();
                    setChanged();
                    notifyObservers(MsgUtil.DISCONNECTED);

                } catch (IOException e) {
                    isConnected = false;
                    synchronized (this) {
                        //notify if connection is succeeded or failed
                        setChanged();
                        notifyObservers(isConnected);
                    }
                }


            }
        });
        fgThread.start();

    }

    public boolean isConnected() {
        return isConnected;
    }



    public void disconnect() {
        fgCommands.add(new Runnable() {
            @Override
            public void run() {
                stop = true;
                isConnected = false;
            }
        });
    }
}
