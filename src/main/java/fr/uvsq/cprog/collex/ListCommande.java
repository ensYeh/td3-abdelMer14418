package fr.uvsq.cprog.collex;

import java.util.List;

// Commande pour lister les machines d'un domaine, avec option de tri

public class ListCommande implements Commande {

    private final Dns dns;
    private final String domaine;
    private final boolean sortByIp; // Vrai si l'option -a est présente

    public ListCommande(Dns dns, String domaine, boolean sortByIp) {
        this.dns = dns;
        this.domaine = domaine;
        this.sortByIp = sortByIp;
    }

    @Override
    public String execute() {
        List<DnsItem> items = dns.getItems(domaine, sortByIp);

        if (items.isEmpty()) {
            return "Aucune machine trouvée pour le domaine : " + domaine;
        }

        StringBuilder sb = new StringBuilder();
        // Affichage au format : adresse.ip nom.qualifie.machine
        for (DnsItem item : items) {
            sb.append(item.getIp().toString())
                    .append(" ")
                    .append(item.getNom().toString())
                    .append("\n");
        }

        // Supprime le dernier saut de ligne
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }
}