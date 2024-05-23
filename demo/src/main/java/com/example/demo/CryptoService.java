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
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptoService {

    @Autowired
    private KeystoreUtil keystoreUtil;

    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

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

    public byte[] encryptRSA(String plainText, PublicKey publicKey, SecureRandom random) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, random);
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }

    public String decryptRSA(byte[] cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(cipherText);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public byte[] encryptAES(String plainText, SecretKey secretKey, byte[] iv, SecureRandom random) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec, random);
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }

    public String decryptAES(byte[] cipherText, SecretKey secretKey, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        byte[] decryptedBytes = cipher.doFinal(cipherText);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public SecureRandom getSecureRandom(String algorithm, Long seed) throws NoSuchAlgorithmException {
        SecureRandom random;
        if (algorithm != null && !algorithm.isEmpty()) {
            random = SecureRandom.getInstance(algorithm);
        } else {
            random = new SecureRandom();
        }
        if (seed != null) {
            random.setSeed(seed);
        }
        return random;
    }

    public KeyPair generateDSAKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    public void storeDSAKeyPair(String alias, KeyPair keyPair, char[] password) throws Exception {
        keystoreUtil.storeDSAKeyPair(alias, keyPair, password);
    }

    public byte[] signData(byte[] data, PrivateKey privateKey, SecureRandom random) throws Exception {
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initSign(privateKey, random);
        signature.update(data);
        return signature.sign();
    }

    public boolean verifySignature(byte[] data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(signatureBytes);
    }

    public List<String> filterAliases(char[] password, String filter) throws Exception {
        List<String> aliases = keystoreUtil.getAliases(password);
        return aliases.stream()
                .filter(alias -> alias.contains(filter))
                .collect(Collectors.toList());
    }

    public void deleteAESKey(String alias, char[] password) throws Exception {
        keystoreUtil.deleteKey(alias, password);
    }

    public void deleteRSAKeyPair(String alias, char[] password) throws Exception {
        keystoreUtil.deleteKey(alias, password);
        keystoreUtil.deletePEMFiles(alias, "rsa");
    }

    public void deleteDSAKeyPair(String alias, char[] password) throws Exception {
        keystoreUtil.deleteKey(alias, password);
        keystoreUtil.deletePEMFiles(alias, "dsa");
    }
}
