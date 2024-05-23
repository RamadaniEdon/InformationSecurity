package com.example.demo;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class KeystoreUtil {

    private static final String KEYSTORE_TYPE = "JCEKS";
    private static final String KEYSTORE_FILE = "keystore.jks";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public void createKeystore(char[] password)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
        keystore.load(null, password);
        try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
            keystore.store(fos, password);
        }
    }

    public void storeSecretKey(String alias, SecretKey secretKey, char[] password) throws Exception {
        KeyStore keystore = loadKeystore(password);
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(password);
        keystore.setEntry(alias, secretKeyEntry, entryPassword);
        try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
            keystore.store(fos, password);
        }
    }

    public SecretKey loadSecretKey(String alias, char[] password) throws Exception {
        KeyStore keystore = loadKeystore(password);
        KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(password);
        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keystore.getEntry(alias, entryPassword);
        return secretKeyEntry != null ? secretKeyEntry.getSecretKey() : null;
    }

    public KeyPair generateRSAKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    public KeyPair generateDSAKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(keySize, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public void storeRSAKeyPair(String alias, KeyPair keyPair, char[] password) throws Exception {
        KeyStore keystore = loadKeystore(password);
        X509Certificate certificate = generateSelfSignedCertificate(keyPair, "SHA256withRSA");
        KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry(
                keyPair.getPrivate(),
                new java.security.cert.Certificate[] { certificate });

        KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(password);
        keystore.setEntry(alias, privateKeyEntry, entryPassword);
        try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
            keystore.store(fos, password);
        }

        savePEMFile(alias + "_public.pem", keyPair.getPublic().getEncoded(), "PUBLIC KEY");
        savePEMFile(alias + "_private.pem", keyPair.getPrivate().getEncoded(), "PRIVATE KEY");
    }

    public void storeDSAKeyPair(String alias, KeyPair keyPair, char[] password) throws Exception {
        KeyStore keystore = loadKeystore(password);
        X509Certificate certificate = generateSelfSignedCertificate(keyPair, "SHA256withDSA");
        KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry(
                keyPair.getPrivate(),
                new java.security.cert.Certificate[] { certificate });

        KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(password);
        keystore.setEntry(alias, privateKeyEntry, entryPassword);
        try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
            keystore.store(fos, password);
        }

        savePEMFile(alias + "_public.pem", keyPair.getPublic().getEncoded(), "PUBLIC KEY");
        savePEMFile(alias + "_private.pem", keyPair.getPrivate().getEncoded(), "PRIVATE KEY");
    }

    private X509Certificate generateSelfSignedCertificate(KeyPair keyPair, String algorithm) throws Exception {
        X500Name issuer = new X500Name("CN=Self-Signed, O=Example Corp, C=US");
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date();
        Date notAfter = new Date(notBefore.getTime() + (365 * 24 * 60 * 60 * 1000L)); // 1 year validity

        ContentSigner signer = new JcaContentSignerBuilder(algorithm).build(keyPair.getPrivate());

        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                serial,
                notBefore,
                notAfter,
                issuer,
                keyPair.getPublic());

        return new JcaX509CertificateConverter()
                .setProvider("BC")
                .getCertificate(certBuilder.build(signer));
    }

    public PrivateKey loadPrivateKey(String alias, char[] password) throws Exception {
        KeyStore keystore = loadKeystore(password);
        KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(password);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keystore.getEntry(alias, entryPassword);
        return privateKeyEntry != null ? privateKeyEntry.getPrivateKey() : null;
    }

    public PublicKey loadPublicKey(String alias, char[] password) throws Exception {
        KeyStore keystore = loadKeystore(password);
        KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(password);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keystore.getEntry(alias, entryPassword);
        return privateKeyEntry != null ? privateKeyEntry.getCertificate().getPublicKey() : null;
    }

    public List<String> getAliases(char[] password) throws Exception {
        KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
        try (FileInputStream fis = new FileInputStream(KEYSTORE_FILE)) {
            keystore.load(fis, password);
        }
        Enumeration<String> aliases = keystore.aliases();
        List<String> aliasList = new ArrayList<>();
        while (aliases.hasMoreElements()) {
            aliasList.add(aliases.nextElement());
        }
        return aliasList;
    }

    private KeyStore loadKeystore(char[] password) throws Exception {
        KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
        try (FileInputStream fis = new FileInputStream(KEYSTORE_FILE)) {
            keystore.load(fis, password);
        }
        return keystore;
    }

    private void savePEMFile(String filename, byte[] encoded, String description) throws IOException {
        String base64Encoded = Base64.getEncoder().encodeToString(encoded);
        String pemContent = "-----BEGIN " + description + "-----\n" + base64Encoded + "\n-----END " + description
                + "-----";
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(pemContent.getBytes());
        }
    }

    public boolean keystoreExists(char[] password) {
        try {
            KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
            try (FileInputStream fis = new FileInputStream(KEYSTORE_FILE)) {
                keystore.load(fis, password);
                return true;
            } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
                return false;
            }
        } catch (KeyStoreException e) {
            return false;
        }
    }

    public List<String> getAllPublicKeyNames() {
        List<String> publicKeyNames = new ArrayList<>();
        Path dir = Paths.get(".");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*_public.pem")) {
            for (Path filePath : stream) {
                String fileName = filePath.getFileName().toString();
                publicKeyNames.add(fileName.substring(0, fileName.length() - 4));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return publicKeyNames;
    }

}
