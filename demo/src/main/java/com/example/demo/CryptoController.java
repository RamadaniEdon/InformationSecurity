package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    @PostMapping("/create-keystore")
    public ResponseEntity<String> createKeystore(@RequestParam String password) {
        try {
            char[] passwordArray = password.toCharArray();
            cryptoService.createKeystore(passwordArray);
            return ResponseEntity.ok("Keystore created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating keystore: " + e.getMessage());
        }
    }

    @PostMapping("/generate/aes")
    public ResponseEntity<String> generateAESKey(@RequestParam int keySize, @RequestParam String alias, @RequestParam String password) {
        try {
            char[] passwordArray = password.toCharArray();
            SecretKey secretKey = cryptoService.generateAESKey(keySize);
            cryptoService.storeAESKey(alias, secretKey, passwordArray);
            return ResponseEntity.ok(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating/storing key: " + e.getMessage());
        }
    }

    @GetMapping("/load/aes")
    public ResponseEntity<String> loadAESKey(@RequestParam String alias, @RequestParam String password) {
        try {
            char[] passwordArray = password.toCharArray();
            SecretKey secretKey = cryptoService.loadAESKey(alias, passwordArray);
            if (secretKey != null) {
                return ResponseEntity.ok(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
            } else {
                return ResponseEntity.badRequest().body("Secret key not found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading key: " + e.getMessage());
        }
    }

    @GetMapping("/aliases")
    public ResponseEntity<List<String>> getAliases(@RequestParam String password) {
        try {
            char[] passwordArray = password.toCharArray();
            List<String> aliases = cryptoService.getAliases(passwordArray);
            return ResponseEntity.ok(aliases);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // New endpoint to generate RSA key pair
    @PostMapping("/generate/rsa")
    public ResponseEntity<String> generateRSAKeyPair(@RequestParam int keySize, @RequestParam String alias, @RequestParam String password) {
        try {
            char[] passwordArray = password.toCharArray();
            System.out.println(keySize);
            KeyPair keyPair = cryptoService.generateRSAKeyPair(keySize);
            System.out.println("mas");
            cryptoService.storeRSAKeyPair(alias, keyPair, passwordArray);
            return ResponseEntity.ok("RSA key pair generated and stored successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating/storing RSA key pair: " + e.getMessage());
        }
    }

    // New endpoint to encrypt data using RSA public key
    @PostMapping("/encrypt/rsa")
    public ResponseEntity<String> encryptRSA(@RequestParam String plainText, @RequestParam String alias, @RequestParam String password) {
        try {
            char[] passwordArray = password.toCharArray();
            PublicKey publicKey = cryptoService.loadPublicKey(alias, passwordArray);
            byte[] encryptedData = cryptoService.encryptRSA(plainText, publicKey);
            return ResponseEntity.ok(Base64.getEncoder().encodeToString(encryptedData));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error encrypting data: " + e.getMessage());
        }
    }

    // New endpoint to decrypt data using RSA private key
    @PostMapping("/decrypt/rsa")
    public ResponseEntity<String> decryptRSA(@RequestParam String cipherText, @RequestParam String alias, @RequestParam String password) {
        try {
            char[] passwordArray = password.toCharArray();
            PrivateKey privateKey = cryptoService.loadPrivateKey(alias, passwordArray);
            byte[] decodedData = Base64.getDecoder().decode(cipherText);
            String decryptedData = cryptoService.decryptRSA(decodedData, privateKey);
            return ResponseEntity.ok(decryptedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error decrypting data: " + e.getMessage());
        }
    }
}
