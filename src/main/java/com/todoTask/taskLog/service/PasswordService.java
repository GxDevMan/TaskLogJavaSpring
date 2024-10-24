package com.todoTask.taskLog.service;

import org.springframework.stereotype.Service;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Service
public class PasswordService {

    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 256;
    private static final int ITERATIONS = 10000;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public String hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, HASH_LENGTH * 8);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    public String[] hashPasswordWithSalt(String password) {
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        return new String[]{hashedPassword, Base64.getEncoder().encodeToString(salt)};
    }

    public boolean verifyPassword(String inputPassword, String storedHash, String storedSalt) {
        byte[] salt = Base64.getDecoder().decode(storedSalt);
        String inputHash = hashPassword(inputPassword, salt);
        return storedHash.equals(inputHash);
    }
}

