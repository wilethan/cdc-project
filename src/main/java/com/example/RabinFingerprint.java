package com.example;

public class RabinFingerprint {
    private static final int WINDOW_SIZE = 48; // Taille de la fenêtre pour le calcul de l'empreinte
    private static final long PRIME = 257; // Nombre premier utilisé pour le calcul de l'empreinte

    private long fingerprint = 0; // Empreinte actuelle

    public RabinFingerprint() {
        // Constructeur
    }

    /**
     * Met à jour l'empreinte digitale avec un nouveau byte.
     *
     * @param data   Le tableau de bytes à traiter.
     * @param length Le nombre de bytes à considérer dans le tableau.
     * @return L'empreinte digitale mise à jour.
     */
    public long update(byte[] data, int length) {
        for (int i = 0; i < length; i++) {
            fingerprint = (fingerprint * PRIME + (data[i] & 0xFF)) % Long.MAX_VALUE;
        }
        return fingerprint;
    }

    /**
     * Réinitialise l'empreinte digitale à zéro.
     */
    public void reset() {
        fingerprint = 0;
    }
}