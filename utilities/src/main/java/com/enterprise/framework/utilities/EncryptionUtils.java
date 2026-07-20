package com.enterprise.framework.utilities;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Symmetric encryption helper.
 */
public final class EncryptionUtils {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    private EncryptionUtils() {
    }

    public static String encrypt(String plainText, String base64Key) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            RANDOM.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(base64Key), "AES"), new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] payload = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, payload, 0, iv.length);
            System.arraycopy(encrypted, 0, payload, iv.length, encrypted.length);
            return Base64.getEncoder().encodeToString(payload);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to encrypt secret", exception);
        }
    }

    public static String decrypt(String encryptedText, String base64Key) {
        try {
            byte[] payload = Base64.getDecoder().decode(encryptedText);
            byte[] iv = new byte[IV_LENGTH];
            byte[] cipherBytes = new byte[payload.length - IV_LENGTH];
            System.arraycopy(payload, 0, iv, 0, IV_LENGTH);
            System.arraycopy(payload, IV_LENGTH, cipherBytes, 0, cipherBytes.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(base64Key), "AES"), new GCMParameterSpec(TAG_LENGTH, iv));
            return new String(cipher.doFinal(cipherBytes), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to decrypt secret", exception);
        }
    }
}
