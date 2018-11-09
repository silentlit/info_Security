package com.company.yihuay;

import java.io.*;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

// Base64和RSA相伴相生,主要是为了防止乱码的产生
// Base64其实不是加密解密算法,只是个编码解码的算法.
// https://www.cnblogs.com/laoA188/p/5775900.html

public class RSAUtils {

    private static String keyPairFile = "keyPair";

    // 保存公私钥对
    private static void saveKeyPair(KeyPair keyPair) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(keyPairFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(keyPair);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 生成公私钥对
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
            keyPairGenerator.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            saveKeyPair(keyPair);
            return keyPair;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取公私钥对
    public static KeyPair getKeyPair() {
        try {
            FileInputStream fileInputStream = new FileInputStream(keyPairFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            KeyPair keyPair = (KeyPair) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return keyPair;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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
