package fr.uvsq.cprog.collex;

import java.util.Objects;

// Représente une entrée complète dans la base de données DNS, associant une AdresseIP à un NomMachine

public class DnsItem {

    private final AdresseIP ip;
    private final NomMachine nom;

    public DnsItem(AdresseIP ip, NomMachine nom) {
        if (ip == null || nom == null) {
            throw new IllegalArgumentException("L'AdresseIP et le NomMachine ne doivent pas etre nuls!");
        }
        this.ip = ip;
        this.nom = nom;
    }

    public AdresseIP getIp() {
        return ip;
    }

    public NomMachine getNom() {
        return nom;
    }

    // Retourne la représentation textuelle de l'item, au format
    // utilisé dans le fichier de base de données : "nom.qualifie.machine
    // adresse.ip"

    @Override
    public String toString() {
        return this.nom.toString() + " " + this.ip.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DnsItem dnsItem = (DnsItem) o;
        // Deux DnsItem sont considérés comme égaux si ils ont la meme ip et le meme
        // nom machine
        return Objects.equals(ip, dnsItem.ip) && Objects.equals(nom, dnsItem.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, nom);
    }

    // Méthode statique pour créer un DnsItem à partir d'une ligne de fichier
    // Format attendu: "nom.qualifie.machine adresse.ip"

    public static DnsItem fromString(String ligne) throws IllegalArgumentException {
        String[] parts = ligne.trim().split("\\s+"); // Sépare par un ou plusieurs espaces

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Format de ligne invalide. Attendu : NomMachine AdresseIP. Reçu : " + ligne);
        }

        // On essaie de créer les objets, les constructeurs gèrent la validation de
        // format
        try {
            NomMachine nom = new NomMachine(parts[0]);
            AdresseIP ip = new AdresseIP(parts[1]);
            return new DnsItem(ip, nom);
        } catch (IllegalArgumentException e) {
            // Repasse l'exception avec un message plus informatif
            throw new IllegalArgumentException(
                    "Erreur de format dans la ligne DNS : " + ligne + " (" + e.getMessage() + ")");
        }
    }
}