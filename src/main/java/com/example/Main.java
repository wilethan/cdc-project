package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Liste des fichiers à tester
        String[] testFiles = { "textfile.txt", "data.csv", "image.jpg", "binary.bin", "logs.log", "archive.zip" };

        // Variables pour le gain de stockage global
        long totalOriginalSize = 0;
        long totalUniqueChunksSize = 0;

        // Boucle pour tester chaque fichier
        for (String testFile : testFiles) {
            System.out.println("=== Test du fichier : " + testFile + " ===");

            // Chemins des fichiers et dossiers
            String filePath = testFile;
            String outputDir = "src/main/resources/chunks_" + testFile.replace(".", "_");
            String reconstructedFilePath = "src/main/resources/reconstructed_" + testFile;

            // Créer une instance de FileChunker
            FileChunker fileChunker = new FileChunker();

            try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath)) {
                if (inputStream == null) {
                    System.err.println("Le fichier '" + filePath + "' n'a pas été trouvé dans le classpath.");
                    continue; // Passer au fichier suivant
                }

                // === Mesurer le temps de découpage ===
                long startTime = System.currentTimeMillis();
                List<byte[]> chunks = fileChunker.chunkFile(inputStream);
                long chunkingTime = System.currentTimeMillis() - startTime;
                System.out.println("Temps de découpage : " + chunkingTime + " ms");

                // === Calculer la taille du fichier d'origine ===
                long originalSize = Files.size(Paths.get("src/main/resources/" + filePath));
                totalOriginalSize += originalSize;
                System.out.println("Taille du fichier d'origine : " + originalSize + " bytes");

                // === Variables pour la compression et les doublons ===
                long totalCompressedSize = 0;
                long totalCompressionTime = 0;
                long uniqueChunksSize = 0;

                // === Enregistrer les chunks compressés sur disque et les indexer dans la base
                // de données ===
                for (int i = 0; i < chunks.size(); i++) {
                    byte[] chunk = chunks.get(i);
                    String hash = HashUtil.calculateSHA256(chunk); // Calculer l'empreinte
                    String fileName = "chunk_" + i + ".snappy"; // Fichier compressé

                    // Vérifier si le chunk existe déjà
                    if (!DatabaseUtil.isChunkExists(hash)) {
                        // === Mesurer le temps de compression ===
                        long startCompressionTime = System.currentTimeMillis();
                        byte[] compressedChunk = CompressionUtil.compress(chunk);
                        long compressionTime = System.currentTimeMillis() - startCompressionTime;

                        totalCompressedSize += compressedChunk.length;
                        totalCompressionTime += compressionTime;

                        // === Enregistrer le chunk compressé sur disque ===
                        fileChunker.saveChunkToDisk(compressedChunk, outputDir, fileName);

                        // === Ajouter le chunk à la base de données ===
                        DatabaseUtil.addChunk(hash, outputDir + "/" + fileName);
                        System.out.println("Chunk " + fileName + " ajouté à la base de données.");
                    } else {
                        System.out.println("Chunk " + fileName + " déjà existant (doublon).");
                    }

                    uniqueChunksSize += chunk.length;
                }

                totalUniqueChunksSize += uniqueChunksSize;

                // === Calculer le gain de stockage pour ce fichier ===
                double storageGain = 100.0 * (originalSize - uniqueChunksSize) / originalSize;
                System.out.println("Gain de stockage pour ce fichier : " + storageGain + " %");

                // === Calculer le taux de compression ===
                double compressionRatio = 100.0 * (originalSize - totalCompressedSize) / originalSize;
                System.out.println("Taux de compression : " + compressionRatio + " %");
                System.out.println("Temps total de compression : " + totalCompressionTime + " ms");

                // === Reconstruire le fichier à partir des chunks ===
                long startReconstructionTime = System.currentTimeMillis();
                ByteArrayOutputStream reconstructedFile = new ByteArrayOutputStream();
                for (int i = 0; i < chunks.size(); i++) {
                    String fileName = "chunk_" + i + ".snappy";
                    Path chunkPath = Paths.get(outputDir, fileName);

                    // Lire le chunk compressé
                    byte[] compressedChunk = Files.readAllBytes(chunkPath);

                    // Décompresser le chunk
                    byte[] chunk = CompressionUtil.decompress(compressedChunk);

                    // Ajouter le chunk au fichier reconstruit
                    reconstructedFile.write(chunk);
                }

                long reconstructionTime = System.currentTimeMillis() - startReconstructionTime;
                System.out.println("Temps de reconstruction : " + reconstructionTime + " ms");

                // === Enregistrer le fichier reconstruit ===
                Files.write(Paths.get(reconstructedFilePath), reconstructedFile.toByteArray());
                System.out.println("Fichier reconstruit enregistré sous : " + reconstructedFilePath);

            } catch (IOException | SQLException e) {
                System.err.println("Erreur lors du traitement du fichier " + testFile + " : " + e.getMessage());
            }

            System.out.println(); // Saut de ligne pour séparer les résultats
        }

        // === Calculer le gain de stockage global ===
        double globalStorageGain = 100.0 * (totalOriginalSize - totalUniqueChunksSize) / totalOriginalSize;
        System.out.println("=== Gain de stockage global === : " + globalStorageGain + " %");
    }
}