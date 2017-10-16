package com.amor.bow.helper;

import com.amor.bow.exception.RegisterPortException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dylan on 2017/10/3.
 */
public final class PortHelper {

    private final static int MIN_PORT = 1024;
    private final static int MAX_PORT = 10000;
    private final static AtomicInteger atomicPort = new AtomicInteger(MIN_PORT);

    public static int registerPort() throws RegisterPortException {
        int port = 0;
        while (true){
            port = atomicPort.getAndIncrement();
            if(port > MAX_PORT){
                throw new RegisterPortException("已无端口可用。");
            }
            if(!localPortUsed(port)){
                break;
            }
        }
        return port;
    }

    public static boolean localPortUsed(int port){
        return portUsed("127.0.0.1",port);
    }
    public static boolean portUsed(String host,int port){

        Socket socket = null;
        try {
            InetAddress Address = InetAddress.getByName(host);
            socket = new Socket(Address,port);  //建立一个Socket连接
            return true;
        } catch (IOException e) {
        }finally {
            if(null != socket && !socket.isClosed()){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    private PortHelper(){}
}
