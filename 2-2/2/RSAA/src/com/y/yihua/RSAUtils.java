package com.y.yihua;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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

    public static PublicKey getPubKCA() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("CApub"));
            PublicKey ca = (PublicKey) objectInputStream.readObject();
            objectInputStream.close();
            return ca;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // base64 -> key
    public static PublicKey base64ToPubK(String base64K) {
        try {
            byte[] bytesOfKey = new BASE64Decoder().decodeBuffer(base64K);
            PublicKey ca = getPubKCA();
            bytesOfKey = decode(ca, bytesOfKey);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytesOfKey);
            RSAPublicKey pubK = (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
            return pubK;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static PrivateKey base64ToPrvK(String base64K) {
        try {
            byte[] bytesOfKey = new BASE64Decoder().decodeBuffer(base64K);
            PublicKey ca = getPubKCA();
            bytesOfKey = decode(ca, bytesOfKey);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bytesOfKey);
            RSAPrivateKey prvK = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            return prvK;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
