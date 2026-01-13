package main.java.dao;

import model.Livre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {
    public void ajouterLivre(Livre livre) throws SQLException {
        String sql = "INSERT INTO livres (titre, auteur, categorie, nombre_exemplaires) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getCategorie());
            stmt.setInt(4, livre.getNombreExemplaires());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) livre.setId(rs.getInt(1));
        }
    }

    public List<Livre> rechercherParTitre(String titre) throws SQLException {
        String sql = "SELECT * FROM livres WHERE titre LIKE ?";
        return executerRequete(sql, "%" + titre + "%");
    }

    public List<Livre> rechercherParAuteur(String auteur) throws SQLException {
        String sql = "SELECT * FROM livres WHERE auteur LIKE ?";
        return executerRequete(sql, "%" + auteur + "%");
    }

    public List<Livre> rechercherParCategorie(String categorie) throws SQLException {
        String sql = "SELECT * FROM livres WHERE categorie LIKE ?";
        return executerRequete(sql, "%" + categorie + "%");
    }

    public List<Livre> tousLesLivresDisponibles() throws SQLException {
        String sql = "SELECT * FROM livres WHERE nombre_exemplaires > 0";
        return executerRequete(sql);
    }

    public List<Livre> tousLesLivres() throws SQLException {
        String sql = "SELECT * FROM livres";
        return executerRequete(sql);
    }

    private List<Livre> executerRequete(String sql, Object... params) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Livre l = new Livre(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getString("categorie"),
                        rs.getInt("nombre_exemplaires")
                );
                livres.add(l);
            }
        }
        return livres;
    }

    public void modifierLivre(Livre livre) throws SQLException {
        String sql = "UPDATE livres SET titre=?, auteur=?, categorie=?, nombre_exemplaires=? WHERE id=?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getCategorie());
            stmt.setInt(4, livre.getNombreExemplaires());
            stmt.setInt(5, livre.getId());
            stmt.executeUpdate();
        }
    }

    public void supprimerLivre(int id) throws SQLException {
        String sql = "DELETE FROM livres WHERE id=?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
