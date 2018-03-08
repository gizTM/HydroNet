package com.senior.gizgiz.hydronet.HelperClass;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Admins on 005 05/03/2018.
 */
public class AESCrypt {
    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    public static String encrypt(String value) throws Exception {
        Key key = generateKey();
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);

    }

    public static String decrypt(String value) throws Exception {
        Key key = generateKey();
        @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        return new String(decryptedByteValue,"utf-8");

    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(AESCrypt.KEY.getBytes(),AESCrypt.ALGORITHM);
    }
}
