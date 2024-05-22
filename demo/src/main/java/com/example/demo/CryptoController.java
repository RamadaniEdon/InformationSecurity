package com.example.demo;

import com.example.demo.VerifyTextRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;
import java.util.List;
import java.security.*;

@RestController
@RequestMapping("/api/crypto")
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private KeystoreUtil keystoreUtil;

    @PostMapping("/create-keystore")
    public ResponseEntity<String> createKeystore(@RequestBody KeystoreRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            cryptoService.createKeystore(passwordArray);
            return ResponseEntity.ok("Keystore created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating keystore: " + e.getMessage());
        }
    }

    @PostMapping("/generate/aes")
    public ResponseEntity<String> generateAESKey(@RequestBody GenerateKeyRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            SecretKey secretKey = cryptoService.generateAESKey(request.getKeySize());
            String aesAlias = "aes_"+request.getAlias();
            cryptoService.storeAESKey(aesAlias, secretKey, passwordArray);
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

    @PostMapping("/encrypt/aes")
    public ResponseEntity<String> encryptAES(@RequestBody EncryptRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            SecretKey secretKey = cryptoService.loadAESKey(request.getAlias(), passwordArray);
            byte[] iv = new byte[12];
            SecureRandom random = cryptoService.getSecureRandom(request.getRandomAlgorithm(), request.getSeed());
            random.nextBytes(iv);
            byte[] encryptedData = cryptoService.encryptAES(request.getPlainText(), secretKey, iv, random);
            String ivAndCipherText = Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encryptedData);
            return ResponseEntity.ok(ivAndCipherText);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error encrypting data: " + e.getMessage());
        }
    }

    @PostMapping("/decrypt/aes")
    public ResponseEntity<String> decryptAES(@RequestBody DecryptRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            SecretKey secretKey = cryptoService.loadAESKey(request.getAlias(), passwordArray);
            String[] parts = request.getCipherText().split(":");
            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] cipherText = Base64.getDecoder().decode(parts[1]);
            String decryptedData = cryptoService.decryptAES(cipherText, secretKey, iv);
            return ResponseEntity.ok(decryptedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error decrypting data: " + e.getMessage());
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

    @PostMapping("/generate/rsa")
    public ResponseEntity<String> generateRSAKeyPair(@RequestBody GenerateKeyRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            KeyPair keyPair = cryptoService.generateRSAKeyPair(request.getKeySize());
            String rsaAlias = "rsa_"+request.getAlias();
            cryptoService.storeRSAKeyPair(rsaAlias, keyPair, passwordArray);
            return ResponseEntity.ok("RSA key pair generated and stored successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating/storing RSA key pair: " + e.getMessage());
        }
    }

    @PostMapping("/encrypt/rsa")
    public ResponseEntity<String> encryptRSA(@RequestBody EncryptRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            PublicKey publicKey = cryptoService.loadPublicKey(request.getAlias(), passwordArray);
            SecureRandom random = cryptoService.getSecureRandom(request.getRandomAlgorithm(), request.getSeed());
            byte[] encryptedData = cryptoService.encryptRSA(request.getPlainText(), publicKey, random);
            return ResponseEntity.ok(Base64.getEncoder().encodeToString(encryptedData));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error encrypting data: " + e.getMessage());
        }
    }

    @PostMapping("/decrypt/rsa")
    public ResponseEntity<String> decryptRSA(@RequestBody DecryptRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            PrivateKey privateKey = cryptoService.loadPrivateKey(request.getAlias(), passwordArray);
            byte[] decodedData = Base64.getDecoder().decode(request.getCipherText());
            String decryptedData = cryptoService.decryptRSA(decodedData, privateKey);
            return ResponseEntity.ok(decryptedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error decrypting data: " + e.getMessage());
        }
    }

    @PostMapping("/generate/dsa")
    public ResponseEntity<String> generateDSAKeyPair(@RequestBody GenerateKeyRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            KeyPair keyPair = cryptoService.generateDSAKeyPair(request.getKeySize());
            // String dsaAlias = "dsa_"+request.getAlias();
            cryptoService.storeDSAKeyPair(request.getAlias(), keyPair, passwordArray);
            return ResponseEntity.ok("DSA key pair generated and stored successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating/storing DSA key pair: " + e.getMessage());
        }
    }

    @PostMapping("/sign-text")
    public ResponseEntity<String> signText(@RequestBody EncryptRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            PrivateKey privateKey = cryptoService.loadPrivateKey(request.getAlias(), passwordArray);
            SecureRandom random = cryptoService.getSecureRandom(request.getRandomAlgorithm(), request.getSeed());
            byte[] data = request.getPlainText().getBytes();
            byte[] signature = cryptoService.signData(data, privateKey, random);
            return ResponseEntity.ok(Base64.getEncoder().encodeToString(signature));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error signing text: " + e.getMessage());
        }
    }

    @PostMapping("/verify-text")
    public ResponseEntity<String> verifyTextSignature(@RequestBody VerifyTextRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            PublicKey publicKey = cryptoService.loadPublicKey(request.getAlias(), passwordArray);
            byte[] data = request.getText().getBytes();
            byte[] signatureBytes = Base64.getDecoder().decode(request.getSignature());
            boolean isValid = cryptoService.verifySignature(data, signatureBytes, publicKey);
            return ResponseEntity.ok(isValid ? "Signature is valid" : "Signature is invalid");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error verifying signature: " + e.getMessage());
        }
    }

    @GetMapping("/rsa/public")
    public ResponseEntity<String> getRSAPublicKey(@RequestParam String alias, @RequestParam String password) {
        try {
            char[] passwordArray = password.toCharArray();
            PublicKey publicKey = cryptoService.loadPublicKey(alias, passwordArray);
            if (publicKey != null) {
                return ResponseEntity.ok(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            } else {
                return ResponseEntity.badRequest().body("Public key not found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading public key: " + e.getMessage());
        }
    }

    @GetMapping("/rsa/private")
    public ResponseEntity<String> getRSAPrivateKey(@RequestParam String alias, @RequestParam String password) {
        try {
            char[] passwordArray = password.toCharArray();
            PrivateKey privateKey = cryptoService.loadPrivateKey(alias, passwordArray);
            if (privateKey != null) {
                return ResponseEntity.ok(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            } else {
                return ResponseEntity.badRequest().body("Private key not found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading private key: " + e.getMessage());
        }
    }

    @PostMapping("/filter-aliases")
    public ResponseEntity<List<String>> filterAliases(@RequestBody FilterAliasesRequest request) {
        try {
            char[] passwordArray = request.getPassword().toCharArray();
            List<String> filteredAliases = cryptoService.filterAliases(passwordArray, request.getFilter());
            return ResponseEntity.ok(filteredAliases);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
