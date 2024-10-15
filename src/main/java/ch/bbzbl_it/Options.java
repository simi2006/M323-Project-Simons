package ch.bbzbl_it;

import ch.bbzbl_it.objects.Dataset;
import ch.bbzbl_it.objects.Feature;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Options {

    OPTION1(1,"1. Auslastung pro Bank (Durchschnitt)") {
        @Override
        public void print(Dataset dataset) {
            var orgEntries = dataset.getFeatures().stream().collect(Collectors.groupingBy(Feature::getSensor_eui)).entrySet();
            System.out.println("Used dataset: " + dataset.getName());
            for (var entry : orgEntries) {
                System.out.printf("\t %4s: \t %.2f \r\n", entry.getKey(), entry.getValue().stream().mapToInt(Feature::getSit).average().getAsDouble());
            }
        }
    },
    OPTION2(2, "2. Auslastung pro Zeit (Durchschnitt)") {
        @Override
        public void print(Dataset dataset) {

        }
    },
    // Spannweiten von einem Grad(20, 21, 22, usw.)
    OPTION3(3, "3. Auslastung bei Temperatur (Spannweiten)") {
        @Override
        public void print(Dataset dataset) {
            var orgEntries = dataset.getFeatures().stream().collect(Collectors.groupingBy(feature -> (int) feature.getTemperature())).entrySet();
            System.out.println("Used dataset: " + dataset.getName());
            for (var entry : orgEntries) {
                System.out.printf("\t %4s: \t %.2f \r\n", entry.getKey().toString(), entry.getValue().stream().mapToInt(Feature::getSit).average().getAsDouble());
            }
        }
    },
    OPTION4(4, "4. Top Ten Auslastung (Wenigste/Meiste)") {
        @Override
        public void print(Dataset dataset) {
//            TODO: Make method
            var orgEntries = dataset.getFeatures().stream().collect(Collectors.groupingBy(feature -> (int) feature.getTemperature())).entrySet();
            System.out.println("Used dataset: " + dataset.getName());
            for (var entry : orgEntries) {
                System.out.printf("\t %4s: \t %.2f \r\n", entry.getKey().toString(), entry.getValue().stream().mapToInt(Feature::getSit).average().getAsDouble());
            }
        }
    };

    private int optionNumber;
    private String describtion;

    Options(int optionNumber, String describtion) {
        this.optionNumber = optionNumber;
        this.describtion = describtion;
    }

    abstract public void print(Dataset dataset);

    private Map produceDimesion(Dataset data) {
        var oneD = data.getFeatures().stream().collect(Collectors.groupingBy(Feature::getSensor_eui));
        var twoD = oneD.entrySet().stream().map(o -> Map.entry(o.getKey(), o.getValue().stream().collect(Collectors.groupingBy(e -> e.getZeitpunkt().getHour())))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return twoD;
    }

    public static Options getOption(int optionNumber) {
        return Stream.of(Options.values()).filter(option -> option.getOptionNumber() == optionNumber).findFirst().orElseThrow(() -> new IllegalArgumentException("Argument " + optionNumber + " isn't valid"));
    }
}
