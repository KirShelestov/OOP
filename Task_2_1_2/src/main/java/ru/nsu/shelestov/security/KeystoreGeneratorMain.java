package ru.nsu.shelestov.security;

public class KeystoreGeneratorMain {
    public static void main(String[] args) {
        try {
            KeystoreGenerator.generateKeystore(
                "server.jks", 
                "serverpass", 
                "serverkey"   
            );
            System.out.println("Keystore successfully generated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}