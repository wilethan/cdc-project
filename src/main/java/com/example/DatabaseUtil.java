package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/chunkdb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    /**
     * Établit une connexion à la base de données.
     *
     * @return Une connexion à la base de données.
     * @throws SQLException Si une erreur de connexion se produit.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Vérifie si un chunk existe déjà dans la base de données.
     *
     * @param hash L'empreinte du chunk à vérifier.
     * @return true si le chunk existe, false sinon.
     * @throws SQLException Si une erreur SQL se produit.
     */
    public static boolean isChunkExists(String hash) throws SQLException {
        String query = "SELECT id FROM chunks WHERE hash = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, hash);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Ajoute un chunk à la base de données.
     *
     * @param hash     L'empreinte du chunk.
     * @param filePath Le chemin du fichier chunk.
     * @throws SQLException Si une erreur SQL se produit.
     */
    public static void addChunk(String hash, String filePath) throws SQLException {
        String query = "INSERT INTO chunks (hash, file_path) VALUES (?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, hash);
            pstmt.setString(2, filePath);
            pstmt.executeUpdate();
        }
    }
}