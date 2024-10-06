package com.portingdeadmods.moreplates.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MPConfig {
    private record IngotPlatePair(ResourceLocation ingot, ResourceLocation plate) {}

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = new File("config/moreplates_ingot_plate_pairs.json");
    private static Set<IngotPlatePair> registeredIngotPlatePairs = new HashSet<>();
    private static Map<ResourceLocation, ResourceLocation> ingotToPlateMap = new HashMap<>();
    private static Map<ResourceLocation, ResourceLocation> plateToIngotMap = new HashMap<>();

    public static void loadConfig() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonArray pairs = json.getAsJsonArray("ingot_plate_pairs");

                if (pairs != null) {
                    for (var entry : pairs) {
                        JsonObject pair = entry.getAsJsonObject();
                        ResourceLocation ingot = ResourceLocation.parse(pair.get("ingot").getAsString());
                        ResourceLocation plate = ResourceLocation.parse(pair.get("plate").getAsString());
                        registeredIngotPlatePairs.add(new IngotPlatePair(ingot, plate));
                        ingotToPlateMap.put(ingot, plate);  // Add mapping for ingot to plate
                        plateToIngotMap.put(plate, ingot);  // Add mapping for plate to ingot
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveIngotPlatePair(ResourceLocation ingotId, ResourceLocation plateId) {
        IngotPlatePair pairKey = new IngotPlatePair(ingotId, plateId);

        // Only save if the pair doesn't already exist
        if (!registeredIngotPlatePairs.contains(pairKey)) {
            registeredIngotPlatePairs.add(pairKey);
            ingotToPlateMap.put(ingotId, plateId); // Update mapping
            plateToIngotMap.put(plateId, ingotId); // Update mapping

            // Create the JSON object to save all pairs
            JsonObject json = new JsonObject();
            JsonArray pairsArray = new JsonArray();

            // Iterate over each unique pair in registeredIngotPlatePairs
            for (IngotPlatePair pair : registeredIngotPlatePairs) {
                JsonObject pairObject = new JsonObject();
                pairObject.addProperty("ingot", pair.ingot().toString()); // Full ingot
                pairObject.addProperty("plate", pair.plate().toString()); // Plate part
                pairsArray.add(pairObject);
            }

            json.add("ingot_plate_pairs", pairsArray);

            // Write the updated JSON to the file
            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(json, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to get the plate from the given ingot
    public static ResourceLocation getPlateFromIngot(ResourceLocation ingotId) {
        return ingotToPlateMap.get(ingotId);
    }

    // Method to get the ingot from the given plate
    public static ResourceLocation getIngotFromPlate(ResourceLocation plateId) {
        return plateToIngotMap.get(plateId);
    }
}
