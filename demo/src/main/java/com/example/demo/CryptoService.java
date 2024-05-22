package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.List;

@Service
public class CryptoService {

    @Autowired
    private KeystoreUtil keystoreUtil;

    public void createKeystore(char[] password) throws Exception {
        keystoreUtil.createKeystore(password);
    }

    public SecretKey generateAESKey(int keySize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keySize);
        return keyGen.generateKey();
    }

    public void storeAESKey(String alias, SecretKey secretKey, char[] password) throws Exception {
        keystoreUtil.storeSecretKey(alias, secretKey, password);
    }

    public SecretKey loadAESKey(String alias, char[] password) throws Exception {
        return keystoreUtil.loadSecretKey(alias, password);
    }

    public KeyPair generateRSAKeyPair(int keySize) throws NoSuchAlgorithmException {
        return keystoreUtil.generateRSAKeyPair(keySize);
    }

    public void storeRSAKeyPair(String alias, KeyPair keyPair, char[] password) throws Exception {
        keystoreUtil.storeRSAKeyPair(alias, keyPair, password);
    }

    public PrivateKey loadPrivateKey(String alias, char[] password) throws Exception {
        return keystoreUtil.loadPrivateKey(alias, password);
    }

    public PublicKey loadPublicKey(String alias, char[] password) throws Exception {
        return keystoreUtil.loadPublicKey(alias, password);
    }

    public List<String> getAliases(char[] password) throws Exception {
        return keystoreUtil.getAliases(password);
    }

    public byte[] encryptRSA(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainText.getBytes());
    }

    public String decryptRSA(byte[] cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(cipherText));
    }

    public KeyPair generateDSAKeyPair(int keySize, SecureRandom random) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(keySize, random);
        return keyPairGenerator.generateKeyPair();
    }

    public void storeDSAKeyPair(String alias, KeyPair keyPair, char[] password) throws Exception {
        keystoreUtil.storeDSAKeyPair(alias, keyPair, password);
    }

    public byte[] signData(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public boolean verifySignature(byte[] data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(signatureBytes);
    }
}
