package com.example;

import java.util.HashMap;
import java.util.Map;

public class ChunkIndex {
    private final Map<Long, String> index = new HashMap<>(); // Empreinte digitale -> Nom du fichier

    /**
     * Ajoute un chunk à l'index.
     *
     * @param fingerprint L'empreinte digitale du chunk.
     * @param fileName    Le nom du fichier où le chunk est stocké.
     */
    public void addChunk(long fingerprint, String fileName) {
        index.put(fingerprint, fileName);
    }

    /**
     * Affiche l'index.
     */
    public void printIndex() {
        for (Map.Entry<Long, String> entry : index.entrySet()) {
            System.out.println("Fingerprint: " + entry.getKey() + " -> File: " + entry.getValue());
        }
    }
}