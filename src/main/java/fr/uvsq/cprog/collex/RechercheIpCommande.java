package fr.uvsq.cprog.collex;

// Commande pour rechercher l'adresse IP d'un nom de machine donné

public class RechercheIpCommande implements Commande {

    private final Dns dns;
    private final NomMachine nom;

    public RechercheIpCommande(Dns dns, NomMachine nom) {
        this.dns = dns;
        this.nom = nom;
    }

    @Override
    public String execute() {
        DnsItem item = dns.getItem(nom);

        if (item == null) {
            return "ERREUR : Nom de machine non trouvé : " + nom;
        }

        return item.getIp().toString();
    }
}