package main.java;


import main.java.dao.EmpruntDAO;
import main.java.dao.LivreDAO;
import main.java.dao.MembreDAO;
import model.Emprunt;
import model.Livre;
import model.Membre;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static LivreDAO livreDAO = new LivreDAO();
    private static MembreDAO membreDAO = new MembreDAO();
    private static EmpruntDAO empruntDAO = new EmpruntDAO();

    public static void main(String[] args) {
        while (true) {
            afficherMenu();
            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1 -> gestionLivres();
                case 2 -> gestionMembres();
                case 3 -> gestionEmprunts();
                case 4 -> rechercheLivres();
                case 5 -> afficherEmpruntsEnRetard();
                case 0 -> {
                    System.out.println("Au revoir !");
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private static void afficherMenu() {
        System.out.println("1. Gestion des Livres");
        System.out.println("2. Gestion des Membres");
        System.out.println("3. Gestion des Emprunts");
        System.out.println("4. Recherche de Livres");
        System.out.println("5. Afficher les emprunts en retard");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    private static void gestionLivres() {
        System.out.println("\n1. Ajouter livre");
        System.out.println("2. Modifier livre");
        System.out.println("3. Supprimer livre");
        System.out.println("4. Afficher tous les livres disponibles");
        System.out.print("Choix : ");
        int c = scanner.nextInt();
        scanner.nextLine();
        try {
            switch (c) {
                case 1 -> {
                    System.out.print("Titre : ");
                    String t = scanner.nextLine();
                    System.out.print("Auteur : ");
                    String a = scanner.nextLine();
                    System.out.print("Catégorie : ");
                    String cat = scanner.nextLine();
                    System.out.print("Exemplaires : ");
                    int n = scanner.nextInt();
                    scanner.nextLine();
                    Livre l = new Livre(t, a, cat, n);
                    livreDAO.ajouterLivre(l);
                    System.out.println("Livre ajouté avec ID: " + l.getId());
                }
                case 2 -> {
                    System.out.print("ID du livre à modifier : ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    // Pour simplifier, on ne gère pas la modification ici en détail
                    System.out.println("Modification non implémentée dans ce menu simplifié.");
                }
                case 3 -> {
                    System.out.print("ID du livre à supprimer : ");
                    int id = scanner.nextInt();
                    livreDAO.supprimerLivre(id);
                    System.out.println("Livre supprimé.");
                }
                case 4 -> {
                    List<Livre> livres = livreDAO.tousLesLivresDisponibles();
                    livres.forEach(Livre::afficherDetails);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void gestionMembres() {
        System.out.println("\n1. Inscrire membre");
        System.out.println("2. Supprimer membre");
        System.out.println("3. Rechercher par nom");
        System.out.print("Choix : ");
        int c = scanner.nextInt();
        scanner.nextLine();
        try {
            switch (c) {
                case 1 -> {
                    System.out.print("Nom : ");
                    String nom = scanner.nextLine();
                    System.out.print("Prénom : ");
                    String prenom = scanner.nextLine();
                    System.out.print("Email : ");
                    String email = scanner.nextLine();
                    System.out.print("Date d'adhésion (AAAA-MM-JJ) : ");
                    String dateStr = scanner.nextLine();
                    LocalDate date = LocalDate.parse(dateStr);
                    Membre m = new Membre(nom, prenom, email, date);
                    membreDAO.inscrireMembre(m);
                    System.out.println("Membre inscrit avec ID: " + m.getId());
                }
                case 2 -> {
                    System.out.print("ID du membre à supprimer : ");
                    int id = scanner.nextInt();
                    membreDAO.supprimerMembre(id);
                    System.out.println("Membre supprimé.");
                }
                case 3 -> {
                    System.out.print("Nom à rechercher : ");
                    String nom = scanner.nextLine();
                    List<Membre> membres = membreDAO.rechercherParNom(nom);
                    membres.forEach(Membre::afficherDetails);
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Format de date invalide.");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private static void gestionEmprunts() {
        System.out.println("\n1. Enregistrer emprunt");
        System.out.println("2. Retourner livre");
        System.out.print("Choix : ");
        int c = scanner.nextInt();
        scanner.nextLine();
        try {
            switch (c) {
                case 1 -> {
                    System.out.print("ID du membre : ");
                    int mid = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("ID du livre : ");
                    int lid = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Date d'emprunt (AAAA-MM-JJ) : ");
                    String dEmp = scanner.nextLine();
                    System.out.print("Date retour prévue (AAAA-MM-JJ) : ");
                    String dRet = scanner.nextLine();
                    LocalDate emp = LocalDate.parse(dEmp);
                    LocalDate ret = LocalDate.parse(dRet);
                    Emprunt e = new Emprunt(mid, lid, emp, ret);
                    empruntDAO.enregistrerEmprunt(e);
                    System.out.println("Emprunt enregistré avec ID: " + e.getIdEmprunt());
                }
                case 2 -> {
                    System.out.print("ID de l'emprunt : ");
                    int eid = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("ID du livre rendu : ");
                    int lid = scanner.nextInt();
                    scanner.nextLine();

                    empruntDAO.gererRetour(eid, lid);
                    System.out.println("Retour enregistré.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void rechercheLivres() {
        System.out.println("\n1. Par titre");
        System.out.println("2. Par auteur");
        System.out.println("3. Par catégorie");
        System.out.print("Choix : ");
        int c = scanner.nextInt();
        scanner.nextLine();
        try {
            System.out.print("Terme de recherche : ");
            String terme = scanner.nextLine();
            List<Livre> resultats;
            switch (c) {
                case 1 -> resultats = livreDAO.rechercherParTitre(terme);
                case 2 -> resultats = livreDAO.rechercherParAuteur(terme);
                case 3 -> resultats = livreDAO.rechercherParCategorie(terme);
                default -> {
                    System.out.println("Choix invalide.");
                    return;
                }
            }
            if (resultats.isEmpty()) {
                System.out.println("Aucun résultat.");
            } else {
                resultats.forEach(Livre::afficherDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void afficherEmpruntsEnRetard() {
        try {
            List<Emprunt> emprunts = empruntDAO.getEmpruntsNonRendus();
            boolean found = false;
            for (Emprunt e : emprunts) {
                long retard = e.calculerRetardEnJours();
                if (retard > 0) {
                    found = true;
                    System.out.printf("Emprunt[ID=%d] - Retard: %d jours → Pénalité: %d F CFA%n",
                            e.getIdEmprunt(), retard, e.calculerPenalite());
                }
            }
            if (!found) System.out.println("Aucun emprunt en retard.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}