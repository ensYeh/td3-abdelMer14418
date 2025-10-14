package fr.uvsq.cprog.collex;

// Exception levée lorsque l'on tente d'ajouter une entrée DNS déja existante
// doublon IP ou Nom

public class DuplicateEntryException extends Exception {
    public DuplicateEntryException(String message) {
        super(message);
    }
}