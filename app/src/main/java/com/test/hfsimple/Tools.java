package com.test.hfsimple;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Tools {
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    public static String Bytes2HexString(byte[] b, int size) {
        String ret = "";
        for (int i = size - 1; i >= 0; i--) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static String decodeUTF8(byte[] bytes) {
        return new String(bytes);
    }

    public static byte[] encodeUTF8(String string) {
        return string.getBytes();
    }

//    public static String Bytes2HexString(byte[] b, int size) {
//        String ret = "";
//        for (int i = 0; i < size; i++) {
//            String hex = Integer.toHexString(b[i] & 0xFF);
//            if (hex.length() == 1) {
//                hex = "0" + hex;
//            }
//            ret += hex.toUpperCase();
//        }
//        return ret;
//    }

//    public static String Bytes2HexString(byte[] bytes, int size) {
//        String ret = "";
//        for (int i = 0; i < size; i++) {
//            String hex = Integer.toString(bytes[i]);
//            if (hex.length() == 1) {
//                hex = "0" + hex;
//            }
//            ret += hex.toUpperCase();
//        }
//        return ret;
//    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static byte[] HexString2Bytes(String src) {
        int len = src.length() / 2;
        byte[] ret = new byte[len];
        byte[] tmp = src.getBytes();

        for (int i = 0; i < len; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static String String2Hex(String str) {
        StringBuffer sb = new StringBuffer();
        //Converting string to character array
        char ch[] = str.toCharArray();
        for(int i = 0; i < ch.length; i++) {
            String hexString = Integer.toHexString(ch[i]);
            sb.append(hexString);
        }
        String result = sb.toString();
        return result;
    }

    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    public static List<String> getStrList(String inputString, int length,
                                          int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }



    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

    public static byte[] concat(byte[]...arrays)
    {
        // Determine the length of the result array
        int totalLength = 0;
        for (int i = 0; i < arrays.length; i++)
        {
            totalLength += arrays[i].length;
        }

        // create the result array
        byte[] result = new byte[totalLength];

        // copy the source arrays into the result array
        int currentIndex = 0;
        for (int i = 0; i < arrays.length; i++)
        {
            System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
            currentIndex += arrays[i].length;
        }

        return result;
    }
}
