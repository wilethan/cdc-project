package com.example;

import org.xerial.snappy.Snappy;

import java.io.IOException;

public class CompressionUtil {
    /**
     * Compresse un tableau de bytes en utilisant Snappy.
     *
     * @param data Les données à compresser.
     * @return Les données compressées.
     * @throws IOException Si une erreur de compression se produit.
     */
    public static byte[] compress(byte[] data) throws IOException {
        return Snappy.compress(data);
    }

    /**
     * Décompresse un tableau de bytes en utilisant Snappy.
     *
     * @param compressedData Les données compressées.
     * @return Les données décompressées.
     * @throws IOException Si une erreur de décompression se produit.
     */
    public static byte[] decompress(byte[] compressedData) throws IOException {
        return Snappy.uncompress(compressedData);
    }
}