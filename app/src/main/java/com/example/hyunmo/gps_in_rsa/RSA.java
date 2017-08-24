package com.example.hyunmo.gps_in_rsa;
/**
 * Created by �̱��� on 2015-05-29 (029).
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
    private KeyPair clsKeyPair;   // public key, private key ��
    private Key clsPublicKey;   // public key
    private Key clsPrivateKey;   // private key

    // generate key
    private RSAPublicKeySpec clsPublicKeySpec;   // �������� public key (module�� exponent)
    private RSAPrivateKeySpec clsPrivateKeySpec;   // �������� private key (module�� exponent)

    // encrypt ����
    String strCipher;   // ����Ʈȭ �� ���� �ٽ� String���� - ��ȣ��

    // decrypt ����
    String strResult;   // ����Ʈȭ �� ���� String���� - ��

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
        // biginteger�� plaintext�� ��ȯ
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
    {// RSA ����Ű/����Ű�� �����Ѵ�.
        try
        {
            // Ű ����
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
    {// ��ȣȭ �Ѵ�.
        try {
            // plaintext�� biginteger�� ��ȯ
            BigInteger b;
            b = new BigInteger(plaintext);
            // ��ȣȭ ����
            BigInteger temp;
            temp = b.modPow(publicKeyExponent, publicKeyModule);
            // biginteger�� plaintext�� ��ȯ
            String n = temp.toString();
            strCipher = n;
            // �������� strCipher�� plaintext string���� ����.
        }
        catch (Exception e) {
        }
    }


    public void decrypt (String encryp_text)
    {// ��ȣȭ �Ѵ�.

        try
        {
            // plaintext�� biginteger�� ��ȯ
            BigInteger b;
            b = new BigInteger(encryp_text);

            // ��ȣȭ ����
            BigInteger temp;
            temp = b.modPow(privateKeyExponent, privateKeyModule);

            // biginteger�� plaintext�� ��ȯ
            String n = temp.toString();

            // �������� strResult�� plaintext string���� ����.
            strResult = n;
        }

        catch (Exception e)
        {
        }
    }

}