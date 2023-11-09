package com.company.bigdata.hbase;


import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;

public class Decode {
    public static void main(String[] args) {
//        String encodedString = "8\\x11*R&\\x89\\xA0laodong.v\\xEE\\xA0-\\xB1\\xF8\\x0D\\x80\\x0C\\xD8Z5l\\xA0webP\\xE3";
//        String decodedString = decodeHex(encodedString);
//        System.out.println(decodedString);


        System.out.println(Arrays.toString(Bytes.toBytes("fasfsdac")));  //[102, 97, 115, 102, 115, 100, 97, 99]
        System.out.println(Bytes.toBytes("f")); // [B@61a52fbd
    }

    public static String decodeHex(String encodedString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encodedString.length(); i += 4) {
            String hex = encodedString.substring(i, i + 4);
            int decimal = Integer.parseInt(hex.substring(2), 16);
            sb.append((char) decimal);
        }
        return sb.toString();
    }
}

