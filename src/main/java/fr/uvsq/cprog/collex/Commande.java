package fr.uvsq.cprog.collex;

// Interface pour le modèle de conception Commande
// Chaque commande utilisateur (recherche, ls, add, quit) implémentera cette
// interface

public interface Commande {

    // Exécute l'action associée à la commande et retourne un résultat sous forme de
    // chaine de caractères

    String execute() throws Exception;

}