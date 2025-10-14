package fr.uvsq.cprog.collex;

// Commande utilisée pour afficher un message d'erreur de syntaxe ou de commande
// inconnue

public class ErreurCommande implements Commande {

    private final String message;

    public ErreurCommande(String message) {
        this.message = "ERREUR : " + message;
    }

    @Override
    public String execute() {
        return this.message;
    }
}