package Class33;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
//import java.xml.bind.DatatypeConverter;

public class Hash {

    private MessageDigest hash;

    public Hash(String algorithm) {
        try {
            hash = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

//    public String hashCode(String input) {
//        return DatatypeConverter.printHexBinary(hash.digest(input.getBytes())).toUpperCase();
//    }

    public static void main(String[] args) {
        System.out.println("支持的算法:");
        for (String str : Security.getAlgorithms("MessageDigest")) {
            System.out.println(str);
        }
        System.out.println("=========");

        String algorithm = "MD5";
        Hash hash = new Hash(algorithm);

        String input1 = "wangxiangweiwangxiangwei1";
        String input2 = "wangxiangweiwangxiangwei2";
        String input3 = "wangxiangweiwangxiangwei3";
        String input4 = "wangxiangweiwangxiangwei4";
        String input5 = "wangxiangweiwangxiangwei5";
//        System.out.println(hash.hashCode(input1));
//        System.out.println(hash.hashCode(input2));
//        System.out.println(hash.hashCode(input3));
//        System.out.println(hash.hashCode(input4));
//        System.out.println(hash.hashCode(input5));
    }
}
