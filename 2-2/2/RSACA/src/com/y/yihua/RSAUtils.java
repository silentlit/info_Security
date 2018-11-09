package com.y.yihua;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;

/**
 * Created by yyh on 18/11/8.
 */
public class RSAUtils
{
    // 公钥加密
    public static byte[] encode(PublicKey publicKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int blockSize = cipher.getBlockSize();
            int outputSize = cipher.getOutputSize(data.length);
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
                }
                ++i;
            }
            return raw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 私钥解密
    public static byte[] decode(PrivateKey privateKey, byte[] raw) {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int blockSize = cipher.getBlockSize();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(64);
            int i = 0;
            while (raw.length - i * blockSize > 0) {
                byteArrayOutputStream.write(cipher.doFinal(raw, i * blockSize, blockSize));
                ++i;
            }
            byte[] tem = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return tem;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 私钥加密
    public static byte[] encode(PrivateKey privateKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            int blockSize = cipher.getBlockSize();
            int outputSize = cipher.getOutputSize(data.length);
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
                }
                ++i;
            }
            return raw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 公钥解密
    public static byte[] decode(PublicKey publicKey, byte[] raw) {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            int blockSize = cipher.getBlockSize();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(64);
            int i = 0;
            while (raw.length - i * blockSize > 0) {
                byteArrayOutputStream.write(cipher.doFinal(raw, i * blockSize, blockSize));
                ++i;
            }
            byte[] tem = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return tem;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
