package com.y.yihua;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Created by yyh on 18/11/8.
 */
public class Client {
    private Key ks = null;

    public Client(Boolean randomKs) {
        if (randomKs) {
            getEncodeKs();
        }
    }

    public void setKs(Key ks) {
        this.ks = ks;
    }

    // 随机Ks
    private void getEncodeKs() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");

            // 生成随机密钥ks
            keyGenerator.init(new SecureRandom());
            this.ks = keyGenerator.generateKey();

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

    // DES加密
    public void encode(String encodeFile, String outputFile) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE,this.ks);
            writeToFile(encodeFile, outputFile, cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DES解密
    public void decode(String decodeFile, String outputFile) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, this.ks);
            writeToFile(decodeFile, outputFile, cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
