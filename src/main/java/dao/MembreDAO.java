package main.java.dao;

import model.Membre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreDAO {
    public void inscrireMembre(Membre membre) throws SQLException {
        String sql = "INSERT INTO membres (nom, prenom, email, date_adhesion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setDate(4, Date.valueOf(membre.getAdhesionDate()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) membre.setId(rs.getInt(1));
        }
    }

    public void supprimerMembre(int id) throws SQLException {
        String sql = "DELETE FROM membres WHERE id=?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Membre> rechercherParNom(String nom) throws SQLException {
        String sql = "SELECT * FROM membres WHERE nom LIKE ?";
        List<Membre> membres = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Membre m = new Membre(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getDate("date_adhesion").toLocalDate()
                );
                membres.add(m);
            }
        }
        return membres;
    }

    public List<Membre> tousLesMembres() throws SQLException {
        String sql = "SELECT * FROM membres";
        List<Membre> membres = new ArrayList<>();
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Membre m = new Membre(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getDate("date_adhesion").toLocalDate()
                );
                membres.add(m);
            }
        }
        return membres;
    }
}
