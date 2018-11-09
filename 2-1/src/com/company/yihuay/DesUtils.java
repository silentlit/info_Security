package com.company.yihuay;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;

public class DesUtils {
    // 密钥文件
    private static String keyFile = "keey";

    // 获取密钥
    private static Key getKey() {
        Key k = null;
        try {
            String fileName = keyFile;
            InputStream inputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            k = (Key) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return k;
    }

    // 保存密钥
    public static void saveKey() {
        SecureRandom secureRandom = new SecureRandom();
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(secureRandom);
            FileOutputStream fileOutputStream = new FileOutputStream(keyFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            Key key = keyGenerator.generateKey();
            objectOutputStream.writeObject(key);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 写入文件
    private static void writeToFile(String inFile, String outFile, Cipher cipher) {
        try {
            InputStream inputStream = new FileInputStream(inFile);
            OutputStream outputStream = new FileOutputStream(outFile);
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cipherInputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, r);
            }
            cipherInputStream.close();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 解密
    public static void decode(String decodeFile, String outputFile) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            writeToFile(decodeFile, outputFile, cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 加密
    public static void encode(String encodeFile, String outputFile) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            writeToFile(encodeFile, outputFile, cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
