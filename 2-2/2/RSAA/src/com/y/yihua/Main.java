package com.y.yihua;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Main {
    public  static int func(int x) {
        return 2 * x;
    }

    public static void main(String[] args) {

        String ipaddress = "192.168.0.5";

        // 接收CA签发的公钥
//        SocketUtils socketUtils = new SocketUtils(1444);
//        socketUtils.getFile("CApubABase64");
//        try {
//            socketUtils.serverSocket.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // 接收B发来的公钥 并验证
        SocketUtils a1 = new SocketUtils(1660);
        a1.getFile("bPubKCABase64");
        PublicKey publicKeyB;
        try {
            a1.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("bPubKCABase64"));
            String base64PubKB = (String) objectInputStream.readObject();
            publicKeyB = RSAUtils.base64ToPubK(base64PubKB);
        } catch (Exception e) {
            e.printStackTrace();
            publicKeyB = null;
        }

        // 发公钥给B
        try {
            Socket socket = new Socket(ipaddress, 1666);
            SocketUtils.sendFile("CApubABase64", socket);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 用B公钥加密x发送
        int x = 20;
        byte[] xx = String.valueOf(x).getBytes();
        byte[] bytes = RSAUtils.encode(publicKeyB, String.valueOf(x).getBytes());
        String strX = new BASE64Encoder().encode(bytes);

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("numOfX"));
            objectOutputStream.writeObject(strX);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Boolean isConnected = false;
        do {
            try {
                Socket socket = new Socket(ipaddress, 1661);
                isConnected = socket.isConnected();
                SocketUtils.sendFile("numOfX", socket);

                System.out.println("s = " + strX + "\n" + "x = " + new String(xx));

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (isConnected == false);

        // A接收f(x) 验证
        ArrayList<String> ss = new ArrayList<String>();
        a1 = new SocketUtils(1662);
        a1.getFile("numOfFx");

//        System.out.println(x);

        try {
            a1.serverSocket.close();

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("numOfFx"));
            String s = (String) objectInputStream.readObject();
            objectInputStream.close();

            bytes = new BASE64Decoder().decodeBuffer(s);
            objectInputStream = new ObjectInputStream(new FileInputStream("aprv"));
            PrivateKey privateKeyA = (PrivateKey) objectInputStream.readObject();
            objectInputStream.close();
            bytes = RSAUtils.decode(privateKeyA, bytes);

            s = new String(bytes);
            int fx = Integer.valueOf(s);
            if (fx != func(x)) {
                System.out.println("not equal");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 加密文件并发送
        long t1 = 0;
        try {
            long startTime = System.currentTimeMillis();

            File file = new File("t.bmp");
            Long len = file.length();
            byte[] fileBytes = new byte[len.intValue()];
            new FileInputStream(file).read(fileBytes);

            // 大坑！！！！ byte[]一定要base64 －－yyh
            String s = new BASE64Encoder().encode(fileBytes);
            bytes = RSAUtils.encode(publicKeyB, s.getBytes());
            s = new BASE64Encoder().encode(bytes);

            long endTime = System.currentTimeMillis();

            t1 = endTime - startTime;

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("encodeT"));
            objectOutputStream.writeObject(s);
            objectOutputStream.close();
            Socket socket = new Socket(ipaddress, 1663);
            SocketUtils.sendFile("encodeT", socket);
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        isConnected = false;
        do {
            try {
                Socket socket = new Socket(ipaddress, 1664);
                isConnected = socket.isConnected();
                SocketUtils.sendMsg(String.valueOf(t1), socket);

                System.out.println(t1);

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (isConnected == false);
    }
}
