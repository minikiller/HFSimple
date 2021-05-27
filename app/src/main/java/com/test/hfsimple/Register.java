package com.test.hfsimple;


import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

public class Register {

    private static Cipher ecipher;
    private static Cipher dcipher;
    private static String secretKey = "4xDy0xo=";
//    private static SecretKey key;

    public static void main(String[] args) {



    }

    public static String encrypt(String str) {
        try {

            // generate secret key using DES algorithm
            byte[] bkey = secretKey.getBytes();
            IvParameterSpec iv = new IvParameterSpec(bkey);
            DESKeySpec desKey = new DESKeySpec(bkey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(desKey);

//            key = KeyGenerator.getInstance("DES").generateKey();
            System.out.println("key: " + key.toString());

            ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            // initialize the ciphers with the given key

            ecipher.init(Cipher.ENCRYPT_MODE, key, iv);

            dcipher.init(Cipher.DECRYPT_MODE, key, iv);

//            String encrypted = encrypt("This is a classified message!");
//            System.out.println("encrypted: " + encrypted);

//            String decrypted = decrypt(encrypted);

//            System.out.println("Decrypted: " + decrypted);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm:" + e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.out.println("No Such Padding:" + e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.println("Invalid Key:" + e.getMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        try {

            // encode the string into a sequence of bytes using the named charset

            // storing the result into a new byte array.

            byte[] utf8 = str.getBytes("UTF8");

            byte[] enc = ecipher.doFinal(utf8);

// encode to base64

            enc = BASE64EncoderStream.encode(enc);

            return new String(enc);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    public static String decrypt(String str) {
        try {

            // generate secret key using DES algorithm

            byte[] bkey = secretKey.getBytes();
            IvParameterSpec iv = new IvParameterSpec(bkey);
            DESKeySpec desKey = new DESKeySpec(bkey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(desKey);

//            key = KeyGenerator.getInstance("DES").generateKey();
            System.out.println("key: " + key.toString());

            ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            // initialize the ciphers with the given key

            ecipher.init(Cipher.ENCRYPT_MODE, key, iv);

            dcipher.init(Cipher.DECRYPT_MODE, key, iv);

//            String encrypted = encrypt("This is a classified message!");
//            System.out.println("encrypted: " + encrypted);
//
//            String decrypted = decrypt(encrypted);
//
//            System.out.println("Decrypted: " + decrypted);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm:" + e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.out.println("No Such Padding:" + e.getMessage());
        } catch (InvalidKeyException e) {
            System.out.println("Invalid Key:" + e.getMessage());
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        try {

            // decode with base64 to get bytes

            byte[] dec = BASE64DecoderStream.decode(str.getBytes());

            byte[] utf8 = dcipher.doFinal(dec);

// create new string based on the specified charset

            return new String(utf8, "UTF8");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

}