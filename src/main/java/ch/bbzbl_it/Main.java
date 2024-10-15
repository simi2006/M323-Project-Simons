package ch.bbzbl_it;

import ch.bbzbl_it.objects.Dataset;
import ch.bbzbl_it.objects.Feature;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("1. Auslastung pro Bank (Durchschnitt)");
        System.out.println("2. Auslastung pro Zeit (Durchschnitt)");
        System.out.println("3. Auslastung bei Temperatur (Spannweiten)");
        System.out.println("4. Top Ten Auslastung (Wenigste/Meiste)");
        System.out.println("Wählen sie eine Option! ");

        // var reader = new DataInputStream(System.in);
        var reader = new Scanner(System.in);
        int input = reader.nextInt();

        var option = Options.getOption(input);

        var file = new File("C:\\Users\\simon\\Downloads\\e05e4cef-6f4e-11ef-956d-005056b0ce82\\data\\taz.view_moveandchill.json");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        var data = gson.fromJson(new FileReader(file), Dataset.class);

        option.print(data);

        var oneD = data.getFeatures().stream().collect(Collectors.groupingBy(Feature::getSensor_eui));

        var twoD = oneD.entrySet().stream().map(o -> Map.entry(o.getKey(), o.getValue().stream().collect(Collectors.groupingBy(e -> e.getZeitpunkt().getHour())))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        for (var feature : data.getFeatures()) {
//            System.out.println(feature.toString());
        }



        System.out.println("Total: " + oneD.size());

    }
}