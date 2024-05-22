package com.example.demo;
import java.util.Objects;

public class KeystoreRequest {
    
    private String password;


    public KeystoreRequest() {
    }

    public KeystoreRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public KeystoreRequest password(String password) {
        setPassword(password);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof KeystoreRequest)) {
            return false;
        }
        KeystoreRequest keystoreRequest = (KeystoreRequest) o;
        return Objects.equals(password, keystoreRequest.password);
    }

    @Override
    public String toString() {
        return "{" +
            " password='" + getPassword() + "'" +
            "}";
    }

}
