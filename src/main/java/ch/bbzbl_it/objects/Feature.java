package ch.bbzbl_it.objects;

import java.time.LocalDateTime;

/**
 * Feature class. Contains properties
 */
public class Feature {

    private Properties properties;

    public long getId() {
        return properties.getId();
    }

    public String getSensor_eui() {
        return properties.getSensor_eui();
    }

    public LocalDateTime getZeitpunkt() {
        return properties.getZeitpunkt();
    }

    public long getObjectid() {
        return properties.getObjectid();
    }

    public double getTemperature() {
        return properties.getTemperature();
    }

    public double getHumidity() {
        return properties.getHumidity();
    }

    public double getLatitude() {
        return properties.getLatitude();
    }

    public double getLongitude() {
        return properties.getLongitude();
    }

    public int getNoise() {
        return properties.getNoise();
    }

    public int getSit() {
        return properties.getSit();
    }

    @Override
    public String toString() {
        return properties.toString().replace("Properties", "Feature");
    }
}
