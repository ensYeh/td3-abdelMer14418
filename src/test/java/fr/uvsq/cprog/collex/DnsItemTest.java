package fr.uvsq.cprog.collex;

import org.junit.Test;
import static org.junit.Assert.*;

public class DnsItemTest {

    @Test
    public void testCreationDnsItemValide() {
        AdresseIP ip = new AdresseIP("193.51.31.90");
        NomMachine nom = new NomMachine("www.uvsq.fr");

        DnsItem item = new DnsItem(ip, nom);

        assertEquals(ip, item.getIp());
        assertEquals(nom, item.getNom());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationDnsItemAvecIpNulle() {
        new DnsItem(null, new NomMachine("www.uvsq.fr"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationDnsItemAvecNomNul() {
        new DnsItem(new AdresseIP("193.51.31.90"), null);
    }

    @Test
    public void testToStringFormat() {
        AdresseIP ip = new AdresseIP("193.51.31.90");
        NomMachine nom = new NomMachine("www.uvsq.fr");
        DnsItem item = new DnsItem(ip, nom);

        // Le format attendu pour le fichier : "nom.qualifie.machine adresse.ip"
        assertEquals("www.uvsq.fr 193.51.31.90", item.toString());
    }

    @Test
    public void testEqualsEtHashCode() {
        AdresseIP ip1 = new AdresseIP("10.0.0.1");
        NomMachine nom1 = new NomMachine("host1.local");

        DnsItem item1 = new DnsItem(ip1, nom1);
        DnsItem item2 = new DnsItem(new AdresseIP("10.0.0.1"), new NomMachine("host1.local"));
        DnsItem item3 = new DnsItem(new AdresseIP("10.0.0.2"), nom1);

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    public void testFromStringValide() {
        String ligne = "poste.uvsq.fr 193.51.31.154";
        DnsItem item = DnsItem.fromString(ligne);

        assertEquals("poste.uvsq.fr", item.getNom().getNomQualifie());
        assertEquals("193.51.31.154", item.getIp().getIpAddress());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringInvalideNombreDeParties() {
        DnsItem.fromString("un seul mot");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringInvalideFormatIP() {
        DnsItem.fromString("serveur.local 999.999.999.999");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromStringInvalideFormatNom() {
        // En supposant que le pattern corrigé rejette cela
        DnsItem.fromString(".debut.local 1.1.1.1");
    }
}