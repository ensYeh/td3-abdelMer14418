package fr.uvsq.cprog.collex;

import java.io.IOException;

// Classe principale

public class DnsApp {

    private final Dns dns;
    private final DnsTUI tui;

    private static final String PROPS_FILE = "config.properties";

    public DnsApp() throws IOException {
        // 1. Initialisation de la base de données DNS
        this.dns = new Dns(PROPS_FILE);
        // 2. Initialisation de l'interface utilisateur
        this.tui = new DnsTUI(this.dns);
    }

    public void run() {
        boolean running = true;
        System.out.println("--- Serveur DNS  (Tapez 'quit' pour sortir) ---");

        while (running) {
            Commande cmd = null;
            String result = null;

            // 1. Récupère la prochaine commande de l'utilisateur
            cmd = tui.nextCommande();

            // 2. Exécute la commande
            try {
                result = cmd.execute();
            } catch (Exception e) {
                // Gestion des exceptions lors de l'exécution (exemple: IOException lors du add)
                result = "ERREUR : Échec de l'exécution de la commande : " + e.getMessage();
            }

            // 3. Affiche le résultat
            tui.affiche(result);

            // 4. Vérifie si l'application doit s'arrêter
            if (cmd instanceof QuitCommande) {
                running = false;
            }
        }

        System.out.println("Extinction du serveur DNS. Au revoir.");
    }

    public static void main(String[] args) {
        try {
            DnsApp app = new DnsApp();
            app.run();
        } catch (IOException e) {
            System.err.println("ERREUR CRITIQUE : Impossible de charger la base de données DNS.");
            System.err.println(
                    "Vérifiez la présence et le contenu du fichier " + PROPS_FILE + " et du fichier de données.");
            System.err.println("Détail de l'erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}