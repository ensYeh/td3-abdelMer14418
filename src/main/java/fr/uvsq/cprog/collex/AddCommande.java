package fr.uvsq.cprog.collex;

// Commande pour ajouter une nouvelle entrée DNS et sauvegarder la base de
// données

public class AddCommande implements Commande {

    private final Dns dns;
    private final AdresseIP ip;
    private final NomMachine nom;

    public AddCommande(Dns dns, AdresseIP ip, NomMachine nom) {
        this.dns = dns;
        this.ip = ip;
        this.nom = nom;
    }

    @Override
    public String execute() throws Exception {
        try {
            // La méthode addItem dans Dns gère les doublons et la sauvegarde
            dns.addItem(ip, nom);
            return ""; // L'énoncé n'affiche rien en cas de succès, juste la ligne vide
        } catch (DuplicateEntryException e) {
            return "ERREUR : " + e.getMessage();
        }
    }
}