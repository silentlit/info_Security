package com.y.yihua;

import sun.misc.BASE64Encoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Main {

    public static void main(String[] args) {
        String ipaddress = "192.168.0.5";
        try {
            // 公私钥对生成
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//            keyPairGenerator.initialize(512);
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("CAprv"));
//            objectOutputStream.writeObject(keyPair.getPrivate());
//            objectOutputStream.close();
//            objectOutputStream = new ObjectOutputStream(new FileOutputStream("CApub"));
//            objectOutputStream.writeObject(keyPair.getPublic());
//            objectOutputStream.close();

            // 读取CA私钥
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("CAprv"));
            PrivateKey privateKeyCA = (PrivateKey) objectInputStream.readObject();
            objectInputStream.close();

            // 读取A公钥并CA签名
            objectInputStream = new ObjectInputStream(new FileInputStream("apub"));
            PublicKey publicKeyA = (PublicKey) objectInputStream.readObject();
            objectInputStream.close();
            byte[] bytes = RSAUtils.encode(privateKeyCA, publicKeyA.getEncoded());
            String CApubABase64 = new BASE64Encoder().encode(bytes);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("CApubABase64"));
            objectOutputStream.writeObject(CApubABase64);
            objectOutputStream.close();

            // 读取B公钥并CA签名
            objectInputStream = new ObjectInputStream(new FileInputStream("bpub"));
            PublicKey publicKeyB = (PublicKey) objectInputStream.readObject();
            objectInputStream.close();
            bytes = RSAUtils.encode(privateKeyCA, publicKeyB.getEncoded());
            String CApubBBase64 = new BASE64Encoder().encode(bytes);
            objectOutputStream = new ObjectOutputStream(new FileOutputStream("CApubBBase64"));
            objectOutputStream.writeObject(CApubBBase64);
            objectOutputStream.close();

            Socket sendToA = new Socket(ipaddress, 1444);
            SocketUtils.sendFile("CApubABase64", sendToA);
            sendToA.close();

            Socket sendToB = new Socket(ipaddress, 1445);
            SocketUtils.sendFile("CApubBBase64", sendToB);
            sendToB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
