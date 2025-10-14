package fr.uvsq.cprog.collex;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class DnsTest {

    private static final String TEMP_PROPS_FILE = "test_dns.properties";
    private static final String TEMP_DATA_FILE = "test_dns_db.txt";
    private Path tempPropsPath;
    private Path tempDataPath;

    @Before
    public void setup() throws IOException {
        tempPropsPath = Paths.get(TEMP_PROPS_FILE);
        tempDataPath = Paths.get(TEMP_DATA_FILE);

        // 1. Créer le fichier de propriétés temporaire
        try (FileWriter writer = new FileWriter(tempPropsPath.toFile())) {
            writer.write("dns.data.file=" + TEMP_DATA_FILE + "\n");
        }

        // 2. Créer le fichier de données initial temporaire
        String initialData = "alpha.domaine.net 10.0.0.1\n" +
                "beta.domaine.net 10.0.0.2\n" +
                "gamma.autre.com 192.168.1.10\n" +
                "delta.domaine.net 10.0.0.3\n";
        Files.write(tempDataPath, initialData.getBytes());
    }

    @After
    public void tearDown() throws IOException {
        // Nettoyer les fichiers temporaires
        Files.deleteIfExists(tempPropsPath);
        Files.deleteIfExists(tempDataPath);
    }

    @Test
    public void testChargementEtRechercheParNom() throws IOException {
        Dns dns = new Dns(TEMP_PROPS_FILE);
        DnsItem item = dns.getItem(new NomMachine("beta.domaine.net"));

        assertNotNull(item);
        assertEquals("10.0.0.2", item.getIp().getIpAddress());
    }

    @Test
    public void testChargementEtRechercheParIp() throws IOException {
        Dns dns = new Dns(TEMP_PROPS_FILE);
        DnsItem item = dns.getItem(new AdresseIP("192.168.1.10"));

        assertNotNull(item);
        assertEquals("gamma.autre.com", item.getNom().getNomQualifie());
    }

    @Test
    public void testGetItemsParDomaineEtTriParNom() throws IOException {
        Dns dns = new Dns(TEMP_PROPS_FILE);
        List<DnsItem> items = dns.getItems("domaine.net", false); // Tri par Nom

        assertEquals(3, items.size());
        assertEquals("alpha.domaine.net", items.get(0).getNom().getNomQualifie());
        assertEquals("beta.domaine.net", items.get(1).getNom().getNomQualifie());
        assertEquals("delta.domaine.net", items.get(2).getNom().getNomQualifie());
    }

    @Test
    public void testGetItemsParDomaineEtTriParIp() throws IOException {
        Dns dns = new Dns(TEMP_PROPS_FILE);
        List<DnsItem> items = dns.getItems("domaine.net", true); // Tri par IP

        assertEquals(3, items.size());
        assertEquals("10.0.0.1", items.get(0).getIp().getIpAddress());
        assertEquals("10.0.0.2", items.get(1).getIp().getIpAddress());
        assertEquals("10.0.0.3", items.get(2).getIp().getIpAddress());
    }

    @Test
    public void testAddItemEtSauvegarde() throws Exception {
        Dns dns = new Dns(TEMP_PROPS_FILE);

        AdresseIP newIp = new AdresseIP("172.16.0.42");
        NomMachine newNom = new NomMachine("newhost.test.com");

        dns.addItem(newIp, newNom);

        // 1. Vérification en mémoire
        assertNotNull(dns.getItem(newNom));

        // 2. Vérification sur disque (en rechargeant)
        Dns dnsReloaded = new Dns(TEMP_PROPS_FILE);
        DnsItem reloadedItem = dnsReloaded.getItem(newIp);
        assertNotNull(reloadedItem);
        assertEquals("newhost.test.com", reloadedItem.getNom().getNomQualifie());

        // Vérification du nombre total d'entrées
        List<String> finalLines = Files.readAllLines(tempDataPath);
        assertEquals(5, finalLines.size()); // 4 initiales + 1 nouvelle
    }

    @Test(expected = DuplicateEntryException.class)
    public void testAddItemDoublonIp() throws Exception {
        Dns dns = new Dns(TEMP_PROPS_FILE);
        // Tente d'ajouter une IP qui existe déjà (10.0.0.1)
        dns.addItem(new AdresseIP("10.0.0.1"), new NomMachine("duplicate.test.com"));
    }

    @Test(expected = DuplicateEntryException.class)
    public void testAddItemDoublonNom() throws Exception {
        Dns dns = new Dns(TEMP_PROPS_FILE);
        // Tente d'ajouter un Nom qui existe déjà (alpha.domaine.net)
        dns.addItem(new AdresseIP("172.16.0.50"), new NomMachine("alpha.domaine.net"));
    }
}