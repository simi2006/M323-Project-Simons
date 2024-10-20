package ch.bbzbl_it.objects;

import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * Class Dataset. Contains the name of the data set and the features
 */
@Data
@Getter
public class Dataset {

    private String name;
    private List<Feature> features;

}
