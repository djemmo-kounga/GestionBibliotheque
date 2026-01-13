package main.java.dao;

import model.Emprunt;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {
    public void enregistrerEmprunt(Emprunt emprunt) throws SQLException {

        LivreDAO livreDAO = new LivreDAO();

        String sql = "INSERT INTO emprunts (membre_id, livre_id, date_emprunt, date_retour_prevue) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, emprunt.getMembreId());
            stmt.setInt(2, emprunt.getLivreId());
            stmt.setDate(3, Date.valueOf(emprunt.getDateEmprunt()));
            stmt.setDate(4, Date.valueOf(emprunt.getDateRetourPrevue()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) emprunt.setIdEmprunt(rs.getInt(1));

            decrementerExemplaires(emprunt.getLivreId());
        }
    }
    public void gererRetour(int empruntId, int livreId) throws SQLException {
        String sql = "UPDATE emprunts SET date_retour_effective = ? WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setInt(2, empruntId);
            stmt.executeUpdate();

            incrementerExemplaires(livreId);
        }
    }

    private void decrementerExemplaires(int livreId) throws SQLException {
        String sql = "UPDATE livres SET nombre_exemplaires = nombre_exemplaires - 1 WHERE id = ? AND nombre_exemplaires > 0";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, livreId);
            stmt.executeUpdate();
        }
    }

    private void incrementerExemplaires(int livreId) throws SQLException {
        String sql = "UPDATE livres SET nombre_exemplaires = nombre_exemplaires + 1 WHERE id = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, livreId);
            stmt.executeUpdate();
        }
    }

    public List<Emprunt> getEmpruntsNonRendus() throws SQLException {
        String sql = "SELECT * FROM emprunts WHERE date_retour_effective IS NULL";
        return chargerEmprunts(sql);
    }

    private List<Emprunt> chargerEmprunts(String sql, Object... params) throws SQLException {
        List<Emprunt> emprunts = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Emprunt e = new Emprunt(
                        rs.getInt("id"),
                        rs.getInt("membre_id"),
                        rs.getInt("livre_id"),
                        rs.getDate("date_emprunt").toLocalDate(),
                        rs.getDate("date_retour_prevue").toLocalDate(),
                        rs.getDate("date_retour_effective") != null ?
                                rs.getDate("date_retour_effective").toLocalDate() : null
                );
                emprunts.add(e);
            }
        }
        return emprunts;
    }
}
