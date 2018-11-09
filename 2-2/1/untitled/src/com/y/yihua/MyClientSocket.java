package com.y.yihua;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by yyh on 18/11/8.
 */
public class MyClientSocket
{
    Socket socket = null;

    public MyClientSocket(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            byte[] bytes = new byte[1024];
            int r;
            while ((r = fileInputStream.read(bytes, 0, bytes.length)) != -1) {
                dataOutputStream.write(bytes, 0, r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String s) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            outputStreamWriter.write(s);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getMsg(Socket socket) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s;
            ArrayList<String> ss = new ArrayList<String>();
            while ((s = bufferedReader.readLine()) != null) {
                ss.add(s);
            }
            bufferedReader.close();
            return ss;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
