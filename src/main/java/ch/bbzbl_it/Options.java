package ch.bbzbl_it;

import ch.bbzbl_it.objects.Dataset;
import ch.bbzbl_it.objects.Feature;
import lombok.Getter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum for the different options which can be selected
 */
@Getter
public enum Options {

    OPTION1(1, "Auslastung pro Bank (Durchschnitt)") {
        @Override
        public void processAndPrint(Dataset dataset) {
            try {
                System.out.println("Used dataset: " + dataset.getName() + "\r\n");
                System.out.printf("\t %16s \t %10s \t %12s \t %12s\r\n", "SensorEUI", "Auslastung", "Breitengrad", "Längengrad");
                dataset.getFeatures()
                        .stream()
                        // Group entries by their sensor_eui
                        .collect(Collectors.groupingBy(Feature::getSensor_eui))
                        .entrySet()
                        .stream()
                        // Map all entries to contain additional info
                        .map(this::generateEntry)
                        // Print each entry
                        .forEach(entry -> System.out.printf(
                                "\t %16s \t %10s \t %12s \t %12s\r\n",
                                entry.getKey().sensor_eui(),
                                // Format average usage to two decimal places
                                Math.round(entry.getValue() * 100) / 100f,
                                entry.getKey().latitude(),
                                entry.getKey().longitude()
                        ));
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage());
                System.out.println("Your data set is incorrect/contains errors");
            }
        }
    },
    OPTION2(2, "Auslastung pro Zeit (Durchschnitt)") {
        @Override
        public void processAndPrint(Dataset dataset) {
            System.out.println("Used dataset: " + dataset.getName() + "\r\n");
            System.out.printf("\t %10s \t %10s \r\n", "Zeitrahmen", "Auslastung");
            dataset.getFeatures()
                    .stream()
                    // Collect features by the hour of the timestamp
                    .collect(Collectors.groupingBy(feature -> feature.getZeitpunkt().getHour()))
                    // Print each entry
                    .forEach((key, value) -> System.out.printf(
                            "\t %16s \t %10s \r\n",
                            key + ":00h - " + key + ":59h",
                            // Calculate the average usage and format it to two decimal places
                            Math.round(averageSit(value) * 100) / 100f
                    ));
        }
    },
    OPTION3(3, "Auslastung bei Temperatur (Spannweiten)") {
        @Override
        public void processAndPrint(Dataset dataset) {
            // Output of the data
            System.out.println("Used dataset: " + dataset.getName() + "\r\n");
            System.out.printf("\t %10s \t %10s \r\n", "Temperatur", "Auslastung");
            // Get features (List with data) from the data set
            dataset.getFeatures()
                    .stream()
                    // Filter out faulty entries
                    .filter(entry -> entry.getTemperature() != -100)
                    // Collect the stream and group it by the temperature
                    .collect(Collectors.groupingBy(feature -> (int) feature.getTemperature()))
                    // Print each entry
                    .forEach((key, value) -> System.out.printf(
                            "\t %10s \t %10s \r\n",
                            key.toString() + " °C",
                            // Calculate the average usage and format it to two decimal places
                            Math.round(averageSit(value) * 100) / 100f
                    ));
        }
    },
    OPTION4(4, "Top Ten Auslastung (Wenigste/Meiste)") {
        @Override
        public void processAndPrint(Dataset dataset) {
            System.out.println("Input 1 for meiste and 2 für wenigste");
            // Read input
            var input = new Scanner(System.in).nextInt();
            // Validate input
            if (input != 1 && input != 2) {
                throw new IllegalArgumentException("Input must be 1 or 2");
            }
            // Determine direction
            final var sortDir = input == 1 ? 1 : -1;

            try {
                System.out.println("Used dataset: " + dataset.getName() + "\r\n");
                System.out.printf("\t %16s \t %10s \t %12s \t %12s\r\n", "SensorEUI", "Auslastung", "Breitengrad", "Längengrad");
                // Get features (List with data) from the data set
                dataset.getFeatures()
                        .stream()
                        // Group entries by their sensor_eui
                        .collect(Collectors.groupingBy(Feature::getSensor_eui))
                        .entrySet()
                        .stream()
                        // Map all entries to contain additional info
                        .map(this::generateEntry)
                        // Sort by value and sorting direction
                        .sorted(((o1, o2) -> o1.getValue().compareTo(o2.getValue()) * sortDir))
                        .limit(10)
                        // Print each entry
                        .forEach(entry -> System.out.printf(
                                "\t %16s \t %10s \t %12s \t %12s\r\n",
                                entry.getKey().sensor_eui(),
                                // Format average usage to two decimal places
                                Math.round(entry.getValue() * 100) / 100f,
                                entry.getKey().latitude(),
                                entry.getKey().longitude()
                        ));
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage());
                System.out.println("Your data set is incorrect/contains errors");
            }
        }
    },
    // Extra option to get the metadata of the data set
    OPTION5(5, "Metadaten") {
        @Override
        public void processAndPrint(Dataset dataset) {
            // Retrieve file
            var meta = Thread.currentThread().getContextClassLoader().getResource("e05e4cef-6f4e-11ef-956d-005056b0ce82/metadata/metadaten.pdf");
            // Open file
            try {
                if (meta != null) {
                    Desktop.getDesktop().open(new File(meta.getFile()));
                    System.out.println("Öffnen erfolreich (Link zum Datenset: https://opendata.swiss/de/dataset/move-and-chill/resource/3d5daaf2-4990-48c9-8291-3d37702c144a)");
                } else {
                    System.out.println("Fehler beim Öffnen der Datei (Link zum Datenset: https://opendata.swiss/de/dataset/move-and-chill/resource/3d5daaf2-4990-48c9-8291-3d37702c144a)");
                }
            } catch (IOException e) {
                System.out.println("Fehler beim Öffnen der Datei (Link zum Datenset: https://opendata.swiss/de/dataset/move-and-chill/resource/3d5daaf2-4990-48c9-8291-3d37702c144a)");
            }
        }
    };

    private final int optionNumber;
    private final String describtion;

    /**
     * Standard constructor
     */
    Options(int optionNumber, String describtion) {
        this.optionNumber = optionNumber;
        this.describtion = describtion;
    }

    /**
     * Method to get option by number
     *
     * @param optionNumber Number of the option
     * @return Found option
     * @throws IllegalArgumentException If there's no option which has number from the parameter
     */
    public static Options getOption(int optionNumber) {
        return Stream
                .of(Options.values())
                // Filter out all options which aren't the input value
                .filter(option -> option.getOptionNumber() == optionNumber)
                // Take first value
                .findFirst()
                // Return value or throw exception
                .orElseThrow(() -> new IllegalArgumentException("Argument " + optionNumber + " isn't valid"));
    }

    /**
     * Method to process and print data
     * @param dataset Data set to process
     */
    abstract public void processAndPrint(Dataset dataset);

    /**
     * Function to calculate average usage from a list of features
     * @param features List of feature to calculate the average from
     * @return Average usage
     */
    Double averageSit(List<Feature> features) {
        return features.stream().mapToInt(Feature::getSit).average().orElseThrow(() -> new NoSuchElementException("Average can't be calculated"));
    }

    /**
     * Method to generate entry with additional information in the key ({@link Options.SensorData}) and the average usage as value
     *
     * @param entry Entry with the sensor_eui as key and list of entries with the corresponding sensor_eui
     * @return Entry with {@link Options.SensorData} as key and the average usage as value
     */
    Map.Entry<SensorData, Double> generateEntry(Map.Entry<String, List<Feature>> entry) {
        final var value = entry.getValue();
        if (!value.isEmpty()) {
            return Map.entry(
                    new SensorData(entry.getKey(), value.getFirst().getLongitude(), value.getFirst().getLatitude()),
                    averageSit(entry.getValue())
            );
        }
        throw new NoSuchElementException("No sensor data found");
    }

    /**
     * Record for additional info
     *
     * @param sensor_eui ID of the sensor
     * @param longitude  Longitude position of the sensor
     * @param latitude   Latitude position of the sensor
     */
    private record SensorData(String sensor_eui, double longitude, double latitude) {
    }

}
