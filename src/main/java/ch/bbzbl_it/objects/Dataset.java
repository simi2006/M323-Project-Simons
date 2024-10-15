package ch.bbzbl_it.objects;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class Dataset {

    private String name;
    private List<Feature> features;

}
