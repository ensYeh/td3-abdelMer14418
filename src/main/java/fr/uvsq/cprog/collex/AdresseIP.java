package fr.uvsq.cprog.collex;

import java.util.Objects;
import java.util.regex.Pattern;

public class AdresseIP implements Comparable<AdresseIP> {
    private static final Pattern IP_PATTERN = Pattern.compile("^(\\d{1,3}\\.){3}\\d{1,3}$");
    private final String ipAddress;

    public AdresseIP(String ipAddress) {
        if (!IP_PATTERN.matcher(ipAddress).matches()) {
            throw new IllegalArgumentException("Format d'adresse IP invalide : " + ipAddress);
        }
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        return ipAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdresseIP adresseIP = (AdresseIP) o;
        return Objects.equals(ipAddress, adresseIP.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress);
    }

    // Pour le tri par adresse IP (ls -a)
    @Override
    public int compareTo(AdresseIP other) {
        return this.ipAddress.compareTo(other.ipAddress);
    }
}