package ru.nsu.shelestov.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;

public class AuthManager {
    private static final Logger logger = Logger.getLogger(AuthManager.class.getName());
    private final Map<String, String> workerCredentials;  

    public AuthManager() {
        this.workerCredentials = new ConcurrentHashMap<>();
    }

    public void registerWorker(String workerId, String token) {
        String hashedToken = hashToken(token);
        workerCredentials.put(workerId, hashedToken);
        logger.info("Registered worker: " + workerId);
    }

    public boolean authenticateWorker(String workerId, String token) {
        String storedHash = workerCredentials.get(workerId);
        if (storedHash == null) {
            logger.warning("Unknown worker attempted to connect: " + workerId);
            return false;
        }
        
        String providedHash = hashToken(token);
        boolean isValid = storedHash.equals(providedHash);
        
        if (!isValid) {
            logger.warning("Authentication failed for worker: " + workerId);
        }
        
        return isValid;
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash token", e);
        }
    }
}