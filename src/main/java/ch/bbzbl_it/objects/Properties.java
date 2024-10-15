package ch.bbzbl_it.objects;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
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
