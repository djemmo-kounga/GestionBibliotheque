package model;

import java.time.LocalDate;

public class Emprunt {
    private int idEmprunt;
    private int membreId;
    private int livreId;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;

    public Emprunt() {}

    public Emprunt(int membreId, int livreId, LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        this.membreId = membreId;
        this.livreId = livreId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public Emprunt(int idEmprunt, int membreId, int livreId, LocalDate dateEmprunt,
                   LocalDate dateRetourPrevue, LocalDate dateRetourEffective) {
        this(membreId, livreId, dateEmprunt, dateRetourPrevue);
        this.idEmprunt = idEmprunt;
        this.dateRetourEffective = dateRetourEffective;
    }

    public int getIdEmprunt() { return idEmprunt; }
    public void setIdEmprunt(int idEmprunt) { this.idEmprunt = idEmprunt; }
    public int getMembreId() { return membreId; }
    public void setMembreId(int membreId) { this.membreId = membreId; }
    public int getLivreId() { return livreId; }
    public void setLivreId(int livreId) { this.livreId = livreId; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(LocalDate dateEmprunt) { this.dateEmprunt = dateEmprunt; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(LocalDate dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }
    public LocalDate getDateRetourEffective() { return dateRetourEffective; }
    public void setDateRetourEffective(LocalDate dateRetourEffective) { this.dateRetourEffective = dateRetourEffective; }

    public long calculerRetardEnJours() {
        if (dateRetourEffective == null) {
            LocalDate now = LocalDate.now();
            if (now.isAfter(dateRetourPrevue)) {
                return java.time.temporal.ChronoUnit.DAYS.between(dateRetourPrevue, now);
            }
        } else if (dateRetourEffective.isAfter(dateRetourPrevue)) {
            return java.time.temporal.ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);
        }
        return 0;
    }

    public int calculerPenalite() {
        return (int) (calculerRetardEnJours() * 100); // 100 F CFA / jour
    }
}
