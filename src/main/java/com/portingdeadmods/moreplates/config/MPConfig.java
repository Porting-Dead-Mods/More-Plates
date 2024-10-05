package com.portingdeadmods.moreplates.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MPConfig {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = new File("config/moreplates_ingot_plate_pairs.json");
    private static Set<String> registeredIngotPlatePairs = new HashSet<>();
    private static Map<String, String> ingotToPlateMap = new HashMap<>();
    private static Map<String, String> plateToIngotMap = new HashMap<>();

    public static void loadConfig() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonArray pairs = json.getAsJsonArray("ingot_plate_pairs");

                if (pairs != null) {
                    for (var entry : pairs) {
                        JsonObject pair = entry.getAsJsonObject();
                        String ingot = pair.get("ingot").getAsString();
                        String plate = pair.get("plate").getAsString();
                        registeredIngotPlatePairs.add(ingot + ":" + plate);
                        ingotToPlateMap.put(ingot, plate);  // Add mapping for ingot to plate
                        plateToIngotMap.put(plate, ingot);  // Add mapping for plate to ingot
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveIngotPlatePair(String ingotId, String plateId) {
        String pairKey = ingotId + ":" + plateId;
        if (!registeredIngotPlatePairs.contains(pairKey)) {
            registeredIngotPlatePairs.add(pairKey);
            ingotToPlateMap.put(ingotId, plateId); // Update mapping
            plateToIngotMap.put(plateId, ingotId); // Update mapping

            JsonObject json = new JsonObject();
            JsonArray pairsArray = new JsonArray();

            for (String pair : registeredIngotPlatePairs) {
                JsonObject pairObject = new JsonObject();
                String[] ids = pair.split(":", 2);
                String ingot = ids[0];
                String plate = ids[1];

                pairObject.addProperty("ingot", ingot);
                pairObject.addProperty("plate", plate);
                pairsArray.add(pairObject);
            }

            json.add("ingot_plate_pairs", pairsArray);

            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(json, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to get the plate from the given ingot
    public static String getPlateFromIngot(String ingotId) {
        return ingotToPlateMap.get(ingotId);
    }

    // Method to get the ingot from the given plate
    public static String getIngotFromPlate(String plateId) {
        return plateToIngotMap.get(plateId);
    }
}
