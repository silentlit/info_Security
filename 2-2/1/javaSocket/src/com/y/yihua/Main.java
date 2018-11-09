package com.y.yihua;

import sun.misc.BASE64Encoder;
import javax.crypto.Cipher;
import java.io.*;
import java.net.Socket;
import java.security.Key;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
    	String ipaddress = "192.168.0.5";
		MyServerSocket server = new MyServerSocket(1344);
		ArrayList<String> ss = new ArrayList<String>();
		Client b = new Client(true);

		// 获取A, n1
		ss = server.getMsg();
		for (String s : ss) {
			System.out.println(s);
		}
		try {
			server.serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 发送ks, A, B, n1, n2
		try {
			Socket socket = new Socket(ipaddress, 1345);

			// 读取km
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("km"));
			Key km = (Key) objectInputStream.readObject();

			// km加密ks
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, km);
			byte[] bytes = cipher.doFinal(b.getKs().getEncoded());

			// base64编码
			String base64Ks = new BASE64Encoder().encode(bytes);
			System.out.print(base64Ks);

			MyServerSocket.sendMsg(base64Ks + "\nA\nB\nn1\nn2", socket);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 接收文件
		server = new MyServerSocket(1346);
		server.getFile("encode");
		b.decode("encode", "decode.bmp");
	}
}
