package com.y.yihua;

import sun.misc.BASE64Decoder;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        String ipaddress = "192.168.0.5";
        int port = 1344;

        MyClientSocket client = new MyClientSocket(ipaddress, port);
        Client a = new Client(false);

        // 发送A, n1 关闭socket
        int n1 = new Random().nextInt();
        client.sendMsg("A\n" + n1);
        try {
            client.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 成为服务端
        try {
            ServerSocket serverSocket = new ServerSocket(1345);
            Socket socket = serverSocket.accept();
            // 接收ks, A, B, n1, n2
            ArrayList<String> ss = new ArrayList<String>();
            ss = MyClientSocket.getMsg(socket);
            for (String s : ss) {
                System.out.println(s);
            }

            // 读取km
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("km"));
            Key km = (Key) objectInputStream.readObject();

            // base64解码
            byte[] bytes = new BASE64Decoder().decodeBuffer(ss.get(0));

            // km解密ks
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, km);
            bytes = cipher.doFinal(bytes);
            try {
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                DESKeySpec keySpec = new DESKeySpec(bytes);
                Key key = keyFactory.generateSecret(keySpec);
                a.setKs(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 发送文件
        client = new MyClientSocket(ipaddress, 1346);
        a.encode("t.bmp", "testEncode");
        client.sendFile("testEncode");
    }
}
