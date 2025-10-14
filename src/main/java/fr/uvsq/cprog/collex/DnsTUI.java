package fr.uvsq.cprog.collex;

import java.util.Scanner;

// Gère les interactions avec l'utilisateur 

public class DnsTUI {

    private final Dns dns;
    private final Scanner scanner;
    private static final String PROMPT = "> ";

    public DnsTUI(Dns dns) {
        this.dns = dns;
        this.scanner = new Scanner(System.in);
    }

    // Lit la prochaine ligne de commande de l'utilisateur, l'analyse
    // et retourne l'objet Commande correspondant

    public Commande nextCommande() {
        System.out.print(PROMPT);

        if (!scanner.hasNextLine()) {
            return new QuitCommande(); // Cas de fin de fichier ou erreur de lecture
        }

        String line = scanner.nextLine().trim();

        if (line.isEmpty()) {
            // Retourne une commande vide ou demande une nouvelle saisie
            return nextCommande();
        }

        String[] parts = line.split("\\s+"); // Sépare par un ou plusieurs espaces
        String firstPart = parts[0];

        try {
            // Cas 1 : Commande 'add'
            if (firstPart.equalsIgnoreCase("add")) {
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Usage: add adresse.ip nom.qualifie.machine");
                }
                AdresseIP ip = new AdresseIP(parts[1]);
                NomMachine nom = new NomMachine(parts[2]);
                return new AddCommande(dns, ip, nom);
            }

            // Cas 2 : Commande 'ls'
            if (firstPart.equalsIgnoreCase("ls")) {
                boolean sortByIp = false;
                String domaine;

                if (parts.length == 2) {
                    // ls domaine
                    domaine = parts[1];
                } else if (parts.length == 3 && parts[1].equals("-a")) {
                    // ls -a domaine
                    sortByIp = true;
                    domaine = parts[2];
                } else {
                    throw new IllegalArgumentException("Usage: ls [-a] domaine");
                }
                // Validation simple du domaine (doit ressembler à un domaine, au moins un point
                // après le début)
                if (domaine.indexOf('.') == -1) {
                    throw new IllegalArgumentException("Format de domaine invalide.");
                }
                return new ListCommande(dns, domaine, sortByIp);
            }

            // Cas 3 : Commande 'quit' ou 'exit'
            if (firstPart.equalsIgnoreCase("quit") || firstPart.equalsIgnoreCase("exit")) {
                return new QuitCommande();
            }

            // Cas 4 & 5 : Recherche (Nom ou IP)
            // On essaie d'abord d'analyser comme une IP
            try {
                AdresseIP ip = new AdresseIP(firstPart);
                // Si l'IP est valide, c'est une recherche de nom
                return new RechercheNomCommande(dns, ip);
            } catch (IllegalArgumentException ipException) {
                // Si ce n'est pas une IP valide, on essaie comme un NomMachine
                try {
                    NomMachine nom = new NomMachine(firstPart);
                    // Si le nom est valide, c'est une recherche d'IP
                    return new RechercheIpCommande(dns, nom);
                } catch (IllegalArgumentException nomException) {
                    // Ni IP, ni NomMachine, ni commande reconnue
                    return new ErreurCommande("Commande ou format non reconnu : " + firstPart);
                }
            }

        } catch (IllegalArgumentException e) {
            return new ErreurCommande("Erreur de syntaxe ou de format: " + e.getMessage());
        } catch (Exception e) {
            // Pour capturer les erreurs inattendues
            return new ErreurCommande("Une erreur inattendue est survenue lors de l'analyse : " + e.getMessage());
        }
    }

    // Affiche le résultat de l'exécution d'une commande.

    public void affiche(String result) {
        if (result != null && !result.isEmpty()) {
            System.out.println(result);
        }
    }
}