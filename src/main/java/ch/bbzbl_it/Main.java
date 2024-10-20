package ch.bbzbl_it;

import ch.bbzbl_it.objects.Dataset;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        var data = extractData();
        printOptions();

        // Main loop
        while (true) {
            var reader = new Scanner(System.in);
            try {
                int input = reader.nextInt();
                Options.getOption(input).print(data);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Invalid input");
            }
            System.out.println("\r\nPress Ctrl+C to exit or input an option ");
        }
    }

    /**
     * Method to return data set extracted from the JSON file
     *
     * @return Data set extracted from the JSON file
     * @throws IOException If the file can't be found
     */
    private static Dataset extractData() throws IOException {

        // Get file from ressources
        ClassLoader classLoader = Main.class.getClassLoader();
        var srcDataFile = classLoader.getResource("e05e4cef-6f4e-11ef-956d-005056b0ce82/data/taz.view_moveandchill.json");

        // Check if file was successfully retrieved
        if (srcDataFile == null) {
            throw new FileNotFoundException("File not found: e05e4cef-6f4e-11ef-956d-005056b0ce82/data/taz.view_moveandchill.json");
        }
        var file = new File(srcDataFile.getFile());

        // Configure and create parser
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        // Parse and return data
        return gson.fromJson(new FileReader(file), Dataset.class);

    }

    /**
     * Method to print options
     */
    private static void printOptions() {
        for (var option : Options.values()) {
            System.out.println(option.getOptionNumber() + ". " + option.getDescribtion());
        }
        System.out.println("Wählen sie eine Option!");
    }

}