package com.zhy.yxkj.security;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Properties;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.stereotype.Component;

@Component
public class Accounts {
    public record User(String id, String role) {
        public boolean manages() { return role.equals("admin") || role.equals("reviewer"); }
    }
    public User authenticate(String username, String password) {
        String file = System.getenv("APP_USERS_FILE");
        if (file == null || username == null || password == null || password.length() > 1024) return null;
        try (InputStream input = Files.newInputStream(Path.of(file))) {
            Properties users = new Properties();
            users.load(input);
            String entry = users.getProperty(username);
            if (entry == null) return null;
            String[] parts = entry.split(",", -1);
            if (parts.length != 4 || !java.util.Set.of("staff", "reviewer", "admin").contains(parts[1])) return null;
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(parts[2]), 310000, 256);
            byte[] hash;
            try { hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
            finally { spec.clearPassword(); }
            return MessageDigest.isEqual(hash, Base64.getDecoder().decode(parts[3])) ? new User(parts[0], parts[1]) : null;
        } catch (Exception error) { return null; }
    }
}
