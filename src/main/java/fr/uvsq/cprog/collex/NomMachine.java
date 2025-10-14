package fr.uvsq.cprog.collex;

import java.util.Objects;
import java.util.regex.Pattern;

// Représente un nom qualifié de machine 

public class NomMachine implements Comparable<NomMachine> {

    private static final Pattern FQDN_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?)+$");

    private final String nomQualifie;
    private final String nomDomaine;
    private final String nomMachineSeul;

    public NomMachine(String nomQualifie) {
        // Validation avec le nouveau motif
        if (!FQDN_PATTERN.matcher(nomQualifie).matches()) {
            throw new IllegalArgumentException("Format de nom qualifié de machine invalide : " + nomQualifie);
        }

        this.nomQualifie = nomQualifie;

        int premierPoint = nomQualifie.indexOf('.');

        this.nomMachineSeul = nomQualifie.substring(0, premierPoint);
        this.nomDomaine = nomQualifie.substring(premierPoint + 1);

        // vérification supplémentaire pour s'assurer que le domaine n'est pas vide

        if (this.nomDomaine.isEmpty()) {
            throw new IllegalArgumentException("Le nom de domaine ne peut pas être vide : " + nomQualifie);
        }
    }

    public String getNomQualifie() {
        return nomQualifie;
    }

    // Retourne le nom de la machine (partie avant le premier point).
    // Exemple : pour "www.uvsq.fr", retourne "www".

    public String getNomMachineSeul() {
        return nomMachineSeul;
    }

    // Retourne le nom de domaine (partie après le premier point)
    // Exemple : pour "www.uvsq.fr", retourne "uvsq.fr"

    public String getNomDomaine() {
        return nomDomaine;
    }

    @Override
    public String toString() {
        return nomQualifie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NomMachine that = (NomMachine) o;
        return Objects.equals(nomQualifie, that.nomQualifie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomQualifie);
    }

    // Pour le tri par nom de machine (ls sans -a)
    @Override
    public int compareTo(NomMachine other) {
        // Le tri naturel des chaînes de caractères sur le nom qualifié est suffisant.
        return this.nomQualifie.compareTo(other.nomQualifie);
    }
}