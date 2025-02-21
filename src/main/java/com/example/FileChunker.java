package com.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileChunker {
    private static final int MIN_CHUNK_SIZE = 64; // Taille minimale d'un chunk
    private static final int MAX_CHUNK_SIZE = 256; // Taille maximale d'un chunk
    private static final int TARGET_CHUNK_SIZE = 128; // Taille cible d'un chunk

    private final RabinFingerprint rabinFingerprint = new RabinFingerprint();

    /**
     * Découpe un fichier en chunks en utilisant l'algorithme CDC.
     *
     * @param filePath Le chemin du fichier à découper.
     * @return Une liste de chunks (tableaux de bytes).
     * @throws IOException Si une erreur de lecture du fichier se produit.
     */
    public List<byte[]> chunkFile(InputStream inputStream) throws IOException {
        List<byte[]> chunks = new ArrayList<>();
        byte[] buffer = new byte[MAX_CHUNK_SIZE];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            int start = 0;
            while (start < bytesRead) {
                int chunkSize = findChunkBoundary(buffer, start, bytesRead);
                byte[] chunk = new byte[chunkSize];
                System.arraycopy(buffer, start, chunk, 0, chunkSize);
                chunks.add(chunk);
                start += chunkSize;
            }
        }
        return chunks;
    }

    /**
     * Trouve la limite d'un chunk en utilisant Rabin Fingerprinting.
     *
     * @param data  Le tableau de bytes à analyser.
     * @param start L'indice de départ dans le tableau.
     * @param end   L'indice de fin dans le tableau.
     * @return La taille du chunk à découper.
     */
    private int findChunkBoundary(byte[] data, int start, int end) {
        rabinFingerprint.reset();
        for (int i = start; i < end; i++) {
            byte[] window = new byte[1];
            window[0] = data[i];
            long fingerprint = rabinFingerprint.update(window, 1);
            if (i - start >= MIN_CHUNK_SIZE && (fingerprint % TARGET_CHUNK_SIZE == 0 || i - start >= MAX_CHUNK_SIZE)) {
                return i - start + 1;
            }
        }
        return end - start;
    }

    public void saveChunksToDisk(List<byte[]> chunks, String outputDir) throws IOException {
        Path dirPath = Paths.get(outputDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath); // Crée le dossier s'il n'existe pas
        }

        for (int i = 0; i < chunks.size(); i++) {
            String chunkFileName = "chunk_" + i + ".bin";
            Path chunkFilePath = dirPath.resolve(chunkFileName);

            try (FileOutputStream fos = new FileOutputStream(chunkFilePath.toFile())) {
                fos.write(chunks.get(i));
            }
        }
    }

    /**
     * Enregistre un chunk sur disque dans le dossier spécifié.
     *
     * @param chunk     Le chunk à enregistrer.
     * @param outputDir Le dossier où enregistrer le chunk.
     * @param fileName  Le nom du fichier pour le chunk.
     * @throws IOException Si une erreur d'écriture se produit.
     */
    public void saveChunkToDisk(byte[] chunk, String outputDir, String fileName) throws IOException {
        Path dirPath = Paths.get(outputDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath); // Crée le dossier s'il n'existe pas
        }

        Path chunkFilePath = dirPath.resolve(fileName);
        try (FileOutputStream fos = new FileOutputStream(chunkFilePath.toFile())) {
            fos.write(chunk);
        }
    }

    public void saveCompressedChunkToDisk(byte[] chunk, String outputDir, String fileName) throws IOException {
        Path dirPath = Paths.get(outputDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath); // Crée le dossier s'il n'existe pas
        }

        // Compresser le chunk
        byte[] compressedChunk = CompressionUtil.compress(chunk);

        // Enregistrer le chunk compressé sur disque
        Path chunkFilePath = dirPath.resolve(fileName);
        try (FileOutputStream fos = new FileOutputStream(chunkFilePath.toFile())) {
            fos.write(compressedChunk);
        }
    }
}