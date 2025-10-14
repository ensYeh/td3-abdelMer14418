package fr.uvsq.cprog.collex;

// Commande pour rechercher le nom de machine d'une adresse IP donnée

public class RechercheNomCommande implements Commande {

    private final Dns dns;
    private final AdresseIP ip;

    public RechercheNomCommande(Dns dns, AdresseIP ip) {
        this.dns = dns;
        this.ip = ip;
    }

    @Override
    public String execute() {
        DnsItem item = dns.getItem(ip);

        if (item == null) {
            return "ERREUR : Adresse IP non trouvée : " + ip;
        }

        return item.getNom().toString();
    }
}