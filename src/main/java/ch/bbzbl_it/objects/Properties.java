package ch.bbzbl_it.objects;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Properties class. Contains all data of a single sensor entry
 */
@Data
@Getter
public class Properties {

    private LocalDateTime zeitpunkt;
    private double temperature;
    private double humidity;
    private double latitude;
    private double longitude;
    private long id;
    private int noise;
    private int sit;
    private long objectid;
    private String sensor_eui;

}
