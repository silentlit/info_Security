package com.y.yihua;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by yyh on 18/11/8.
 */
public class SocketUtils
{
    ServerSocket serverSocket = null;

    Socket socket = null;

    public SocketUtils(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.socket = serverSocket.accept();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFile(String fileName) {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            byte[] bytes = new byte[1024];
            int r;
            while ((r = dis.read(bytes, 0, bytes.length)) != -1) {
                fileOutputStream.write(bytes, 0, r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendFile(String fileName, Socket socket) {
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

    public ArrayList<String> getMsg() {
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

    public static void sendMsg(String s, Socket socket) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            outputStreamWriter.write(s);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
