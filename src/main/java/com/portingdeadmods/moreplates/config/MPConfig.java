package com.portingdeadmods.moreplates.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.portingdeadmods.moreplates.MorePlatesMod;
import net.neoforged.fml.loading.FMLLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MPConfig {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = new File("config/moreplates/ingot_plate_pairs.json");
    private static final File generatorConfigFile = new File("config/moreplates/moreplates-generator.json");
    private static final List<String> generatorValues = new ArrayList<>();
    private static Set<String> registeredIngotPlatePairs = new HashSet<>();
    private static Map<String, String> ingotToPlateMap = new HashMap<>();
    private static Map<String, String> plateToIngotMap = new HashMap<>();

    public static void loadConfig() {
        FMLLoader.getGamePath().resolve("config").resolve(MorePlatesMod.MODID).toFile().mkdirs();
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
                        ingotToPlateMap.put(ingot, plate);
                        plateToIngotMap.put(plate, ingot);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadGeneratorConfig() {
        FMLLoader.getGamePath().resolve("config").resolve(MorePlatesMod.MODID).toFile().mkdirs();

        if (!generatorConfigFile.exists()) {
            try (FileWriter writer = new FileWriter(generatorConfigFile)) {
                JsonArray emptyArray = new JsonArray();
                gson.toJson(emptyArray, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (FileReader reader = new FileReader(generatorConfigFile)) {
                JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
                if (jsonArray != null) {
                    generatorValues.clear();
                    for (var entry : jsonArray) {
                        generatorValues.add(entry.getAsString());
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
            ingotToPlateMap.put(ingotId, plateId);
            plateToIngotMap.put(plateId, ingotId);

            JsonObject json = new JsonObject();
            JsonArray pairsArray = new JsonArray();

            for (String pair : registeredIngotPlatePairs) {
                int colonIndex = pair.indexOf(':');
                String ingot = pair.substring(0, colonIndex);
                String remaining = pair.substring(colonIndex + 1);

                colonIndex = remaining.indexOf(':');
                ingotId = remaining.substring(0, colonIndex);
                plateId = remaining.substring(colonIndex + 1);

                JsonObject pairObject = new JsonObject();
                pairObject.addProperty("ingot", ingot + ":" + ingotId);
                pairObject.addProperty("plate", plateId);
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

    public static List<String> getGeneratorValues() {
        return generatorValues;
    }

    public static String getPlateFromIngot(String ingotId) {
        return ingotToPlateMap.get(ingotId);
    }

    public static String getIngotFromPlate(String plateId) {
        return plateToIngotMap.get(plateId);
    }


}
