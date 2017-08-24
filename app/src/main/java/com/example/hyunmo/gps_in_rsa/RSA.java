package com.example.hyunmo.gps_in_rsa;
/**
 * Created by 이근태 on 2015-05-29 (029).
 */
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSA {

    // initiate
    private KeyPair clsKeyPair;   // public key, private key 쌍
    private Key clsPublicKey;   // public key
    private Key clsPrivateKey;   // private key

    // generate key
    private RSAPublicKeySpec clsPublicKeySpec;   // 실질적인 public key (module과 exponent)
    private RSAPrivateKeySpec clsPrivateKeySpec;   // 실질적인 private key (module과 exponent)

    // encrypt 관련
    String strCipher;   // 바이트화 한 것을 다시 String으로 - 암호문

    // decrypt 관련
    String strResult;   // 바이트화 한 평문을 String으로 - 평문

    // generate key
    private BigInteger publicKeyModule;
    private BigInteger publicKeyExponent;
    private BigInteger privateKeyModule;
    private BigInteger privateKeyExponent;

    public void pushPublicKey(String s, int numbering){
        BigInteger Module, Exponent;
        if (numbering == 1){
            Module = new BigInteger(s);
            publicKeyModule = Module;
        }
        else if (numbering == 2){
            Exponent = new BigInteger(s);
            publicKeyExponent = Exponent;
        }
    }

    public String getPublicKeyExponent(){
        // biginteger를 plaintext로 변환
        String n = publicKeyExponent.toString();
        return n;
    }

    public String getPublicKeyModule(){
        String n = publicKeyModule.toString();
        return n;
    }

    public String getStrCipher ()
    {
        return strCipher;
    }

    public String getStrResult ()
    {
        return strResult;
    }

    public void GenKey ()
    {// RSA 공개키/개인키를 생성한다.
        try
        {
            // 키 생성
            KeyPairGenerator clsKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
            clsKeyPairGenerator.initialize(512);

            clsKeyPair = clsKeyPairGenerator.genKeyPair();
            clsPublicKey = clsKeyPair.getPublic();
            clsPrivateKey = clsKeyPair.getPrivate();
            KeyFactory fact = KeyFactory.getInstance("RSA");

            clsPublicKeySpec = fact.getKeySpec(clsPublicKey, RSAPublicKeySpec.class);
            clsPrivateKeySpec = fact.getKeySpec(clsPrivateKey, RSAPrivateKeySpec.class);

            publicKeyModule = clsPublicKeySpec.getModulus();
            publicKeyExponent = clsPublicKeySpec.getPublicExponent();

            privateKeyModule = clsPrivateKeySpec.getModulus();
            privateKeyExponent = clsPrivateKeySpec.getPrivateExponent();
        }
        catch (Exception e)
        {
        }
    }

    public void encrypt (String plaintext)
    {// 암호화 한다.
        try {
            // plaintext를 biginteger로 변환
            BigInteger b;
            b = new BigInteger(plaintext);
            // 암호화 수행
            BigInteger temp;
            temp = b.modPow(publicKeyExponent, publicKeyModule);
            // biginteger를 plaintext로 변환
            String n = temp.toString();
            strCipher = n;
            // 전역변수 strCipher에 plaintext string형을 저장.
        }
        catch (Exception e) {
        }
    }


    public void decrypt (String encryp_text)
    {// 복호화 한다.

        try
        {
            // plaintext를 biginteger로 변환
            BigInteger b;
            b = new BigInteger(encryp_text);

            // 복호화 수행
            BigInteger temp;
            temp = b.modPow(privateKeyExponent, privateKeyModule);

            // biginteger를 plaintext로 변환
            String n = temp.toString();

            // 전역변수 strResult에 plaintext string형을 저장.
            strResult = n;
        }

        catch (Exception e)
        {
        }
    }

}