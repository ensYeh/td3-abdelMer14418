package fr.uvsq.cprog.collex;

// Commande pour s'arreter 

public class QuitCommande implements Commande {

    // Un simple flag qui indique à DnsApp de s'arrter
    public static final String MESSAGE_QUIT = "Good Bye...";

    public QuitCommande() {
        // Cette commande n'a pas besoin de l'instance Dns
    }

    @Override
    public String execute() {
        return MESSAGE_QUIT;
    }
}