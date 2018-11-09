package com.y.yihua;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * Created by yyh on 18/11/8.
 */
public class SocketUtils {
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
}
