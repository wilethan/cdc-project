package com.example;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;

public class HashUtil {
    /**
     * Calcule l'empreinte SHA-256 d'un chunk.
     *
     * @param data Le chunk à hacher.
     * @return L'empreinte SHA-256 sous forme de chaîne hexadécimale.
     */
    public static String calculateSHA256(byte[] data) {
        SHA256Digest digest = new SHA256Digest();
        digest.update(data, 0, data.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return Hex.toHexString(hash);
    }
}