package com.y.yihua;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Main {

    public static int func(int x) {
        return 2 * x;
    }

    public static void main(String[] args) {

        String ipaddress = "192.168.0.5";

        // 接收CA签发的公钥
//        SocketUtils socketUtils = new SocketUtils(1445);
//        socketUtils.getFile("CApubBBase64");
//        try {
//            socketUtils.serverSocket.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // 发公钥给A
        try {
            Socket socket = new Socket(ipaddress, 1660);
            SocketUtils.sendFile("CApubBBase64", socket);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 接收A发来的公钥 并验证
        SocketUtils b1 = new SocketUtils(1666);
        b1.getFile("aPubKCABase64");
        PublicKey publicKeyA;
        try {
            b1.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("aPubKCABase64"));
            String base64PubKB = (String) objectInputStream.readObject();
            publicKeyA = RSAUtils.base64ToPubK(base64PubKB);
        } catch (Exception e) {
            e.printStackTrace();
            publicKeyA = null;
        }

        // 接收x 用A公钥加密f(x)发送
        ArrayList<String> ss = new ArrayList<String>();
        b1 = new SocketUtils(1661);
        b1.getFile("numOfX");
        try {
            b1.serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("numOfX"));
            String s = (String) objectInputStream.readObject();
            objectInputStream.close();

            System.out.println("s = " + s);

            objectInputStream = new ObjectInputStream(new FileInputStream("bprv"));
            PrivateKey privateKeyB = (PrivateKey) objectInputStream.readObject();
            objectInputStream.close();
            byte[] bytesX = new BASE64Decoder().decodeBuffer(s);
            byte[] xx = RSAUtils.decode(privateKeyB, bytesX);
            s = new String(xx);
            int x = Integer.valueOf(s);

            System.out.println(x);

            x = func(x);
            bytesX = RSAUtils.encode(publicKeyA, String.valueOf(x).getBytes());
            Socket socket = new Socket(ipaddress, 1662);

            s = new BASE64Encoder().encode(bytesX);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("numOfFx"));
            objectOutputStream.writeObject(s);
            objectOutputStream.close();

            SocketUtils.sendFile("numOfFx", socket);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 接收文件
        b1 = new SocketUtils(1663);
        b1.getFile("encodeT");
        try {
            b1.serverSocket.close();
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("bprv"));
            PrivateKey privateKeyB = (PrivateKey) objectInputStream.readObject();
            objectInputStream.close();

            long startTime = System.currentTimeMillis();

            objectInputStream = new ObjectInputStream(new FileInputStream("encodeT"));
            String s = (String) objectInputStream.readObject();
            objectInputStream.close();
            byte[] bytes = new BASE64Decoder().decodeBuffer(s);
            bytes = RSAUtils.decode(privateKeyB, bytes);
            s = new String(bytes);
            bytes = new BASE64Decoder().decodeBuffer(s);

            long endTime = System.currentTimeMillis();

            FileOutputStream fileOutputStream = new FileOutputStream("decode");
            fileOutputStream.write(bytes);
            fileOutputStream.close();

            long t2 = endTime - startTime;
            b1 = new SocketUtils(1664);
            ss = b1.getMsg();

            for (String str : ss) {
                System.out.println(str);
            }

            long t1 = Long.parseLong(ss.get(0));
            double t = (t1 + t2) / 1000.0;
            System.out.println("time: " + t + "s");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
