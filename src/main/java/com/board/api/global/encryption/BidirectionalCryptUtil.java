package com.board.api.global.encryption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class BidirectionalCryptUtil {

    private static final String AES = "AES";
    private static final java.nio.charset.Charset UTF_8 = StandardCharsets.UTF_8;

    @Value("${crypt.alg}")
    private String algorithm;

    @Value("${crypt.key}")
    private String secretKey;

    @Value("${crypt.iv}")
    private String initializationVector;

    public String encrypt(String raw) throws Exception {
        Cipher cipher = initializeCipher(Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(raw.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encrypted) throws Exception {
        Cipher cipher = initializeCipher(Cipher.DECRYPT_MODE);
        byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, UTF_8);
    }

    private Cipher initializeCipher(int mode) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(UTF_8), AES);
        IvParameterSpec ivParamSpec = new IvParameterSpec(initializationVector.getBytes(UTF_8));
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(mode, keySpec, ivParamSpec);
        return cipher;
    }
}