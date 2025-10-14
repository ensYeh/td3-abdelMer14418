package fr.uvsq.cprog.collex;

import org.junit.Test;
import static org.junit.Assert.*;

public class NomMachineTest {

    @Test
    public void testCreationNomValide() {
        NomMachine nm = new NomMachine("www.uvsq.fr");
        assertEquals("www.uvsq.fr", nm.getNomQualifie());
        assertEquals("www", nm.getNomMachineSeul());
        assertEquals("uvsq.fr", nm.getNomDomaine());
        assertEquals("www.uvsq.fr", nm.toString());
    }

    @Test
    public void testCreationNomValideAvecSousDomaine() {
        NomMachine nm = new NomMachine("serveur-prod.salle1.monentreprise.com");
        assertEquals("serveur-prod.salle1.monentreprise.com", nm.getNomQualifie());
        // Le domaine commence après le premier point
        assertEquals("serveur-prod", nm.getNomMachineSeul());
        assertEquals("salle1.monentreprise.com", nm.getNomDomaine());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationNomInvalideSansPoint() {
        new NomMachine("unseulnom");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationNomInvalidePointAuDebut() {
        new NomMachine(".uvsq.fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreationNomInvalidePointALaFin() {
        new NomMachine("www.uvsq.fr.");
    }

    @Test
    public void testEqualsEtHashCode() {
        NomMachine nm1 = new NomMachine("pikachu.uvsq.fr");
        NomMachine nm2 = new NomMachine("pikachu.uvsq.fr");
        NomMachine nm3 = new NomMachine("raichu.uvsq.fr");

        assertEquals(nm1, nm2);
        assertNotEquals(nm1, nm3);
        assertEquals(nm1.hashCode(), nm2.hashCode());
    }

    @Test
    public void testCompareTo() {
        NomMachine nm1 = new NomMachine("abc.domaine.fr");
        NomMachine nm2 = new NomMachine("def.domaine.fr");

        assertTrue(nm1.compareTo(nm2) < 0);
        assertTrue(nm2.compareTo(nm1) > 0);
        assertEquals(0, nm1.compareTo(new NomMachine("abc.domaine.fr")));
    }
}