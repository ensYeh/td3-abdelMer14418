package fr.uvsq.cprog.collex;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// Gère la bdd DNS, y compris le chargement, la recherche et la sauvegarde.

public class Dns {

    // Utilisation de deux Maps pour les recherches rapides dans les deux sens
    private final Map<NomMachine, DnsItem> itemsByNom;
    private final Map<AdresseIP, DnsItem> itemsByIp;
    private final Path dataFilePath;

    public Dns(String propertiesFileName) throws IOException {

        // 1. Charger les propriétés
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(propertiesFileName)) {
            props.load(fis);
        }

        String fileName = props.getProperty("dns.data.file");
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("La propriété 'dns.data.file' n'est pas définie dans " + propertiesFileName);
        }

        this.dataFilePath = Paths.get(fileName);
        this.itemsByNom = new ConcurrentHashMap<>();
        this.itemsByIp = new ConcurrentHashMap<>();

        // 2. Charger la base de données
        chargerBaseDeDonnees();
    }

    // --- Méthodes de persistance ---

    private void chargerBaseDeDonnees() throws IOException {
        if (!Files.exists(dataFilePath)) {
            // Crée le fichier s'il n'existe pas, et initialise les maps vides.
            Files.createFile(dataFilePath);
            return;
        }

        List<String> lignes = Files.readAllLines(dataFilePath);
        for (String ligne : lignes) {
            if (ligne.trim().isEmpty())
                continue;
            try {
                DnsItem item = DnsItem.fromString(ligne);
                // Ajout direct aux maps (on suppose que le fichier ne contient pas de doublons
                // illégaux)
                this.itemsByNom.put(item.getNom(), item);
                this.itemsByIp.put(item.getIp(), item);
            } catch (IllegalArgumentException e) {
                System.err.println("Avertissement: Ligne ignorée (format invalide) dans le fichier DNS : " + ligne);
            }
        }
    }

    private void sauvegarderBaseDeDonnees() throws IOException {
        // Créer une liste de toutes les lignes au format "NomMachine AdresseIP"
        List<String> lignes = this.itemsByNom.values().stream()
                .map(DnsItem::toString)
                .collect(Collectors.toList());

        // Écrit toutes les lignes dans le fichier, écrasant l'ancien contenu
        Files.write(dataFilePath, lignes);
    }

    // --- Méthodes de recherche ---

    // Retourne l'item DNS pour un nom de machine donné

    public DnsItem getItem(NomMachine nom) {
        return itemsByNom.get(nom);
    }

    // Retourne l'item DNS pour une adresse IP donnée

    public DnsItem getItem(AdresseIP ip) {
        return itemsByIp.get(ip);
    }

    // Retourne la liste des items DNS pour un domaine donné

    public List<DnsItem> getItems(String domaine, boolean sortByIp) {

        List<DnsItem> result = new ArrayList<>();
        // Filtrer tous les items pour ceux qui appartiennent au domaine
        for (DnsItem item : itemsByNom.values()) {
            if (item.getNom().getNomDomaine().equals(domaine)) {
                result.add(item);
            }
        }

        // Trier la liste
        if (sortByIp) {
            // Tri par AdresseIP (utilise le compareTo d'AdresseIP)
            Collections.sort(result, Comparator.comparing(DnsItem::getIp));
        } else {
            // Tri par NomMachine (utilise le compareTo de NomMachine)
            Collections.sort(result, Comparator.comparing(DnsItem::getNom));
        }

        return result;
    }

    // --- Méthode d'ajout ---

    // Ajoute une nouvelle entrée DNS à la base de données et la sauvegarde sur
    // disque

    public void addItem(AdresseIP ip, NomMachine nom) throws DuplicateEntryException, IOException {

        // 1. Vérification des doublons
        if (itemsByIp.containsKey(ip)) {
            throw new DuplicateEntryException("L'adresse IP existe déjà : " + ip);
        }
        if (itemsByNom.containsKey(nom)) {
            throw new DuplicateEntryException("Le nom de machine existe déjà : " + nom);
        }

        // 2. Ajout dans les maps
        DnsItem newItem = new DnsItem(ip, nom);
        itemsByIp.put(ip, newItem);
        itemsByNom.put(nom, newItem);

        // 3. Sauvegarde sur disque
        sauvegarderBaseDeDonnees();
    }
}