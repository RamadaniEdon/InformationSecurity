package com.example.demo;

public class EncryptRequest {
    private String plainText;
    private String alias;
    private String password;
    private String randomAlgorithm;
    private Long seed;

    // Getters and setters
    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRandomAlgorithm() {
        return randomAlgorithm;
    }

    public void setRandomAlgorithm(String randomAlgorithm) {
        this.randomAlgorithm = randomAlgorithm;
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }
}
