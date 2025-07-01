package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    // 生成盐
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 使用 SHA-256 加密密码，带盐
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes()); // 添加盐
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword); // 返回 Base64 编码的哈希值
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("加密算法不存在", e);
        }
    }

    // 校验密码
    public static boolean checkPassword(String plainPassword, String hashedPassword, String salt) {
        String newHash = hashPassword(plainPassword, salt);
        return newHash.equals(hashedPassword); // 比较加密后的密码
    }
}
