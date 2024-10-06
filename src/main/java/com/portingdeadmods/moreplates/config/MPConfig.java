package com.portingdeadmods.moreplates.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import com.portingdeadmods.moreplates.MorePlatesMod;
import net.neoforged.fml.loading.FMLLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MPConfig {
    private record IngotPlatePair(ResourceLocation ingot, ResourceLocation plate) {}

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = new File("config/moreplates/ingot_plate_pairs.json");
    private static final File generatorConfigFile = new File("config/moreplates/moreplates-generator.json");
    private static final List<String> generatorValues = new ArrayList<>();
    private static final Set<IngotPlatePair> registeredIngotPlatePairs = new HashSet<>();
    private static final Map<ResourceLocation, ResourceLocation> ingotToPlateMap = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> plateToIngotMap = new HashMap<>();

    public static void loadConfig() {
        FMLLoader.getGamePath().resolve("config").resolve(MorePlatesMod.MODID).toFile().mkdirs();
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
                JsonArray defaultArray = new JsonArray();
                defaultArray.add("minecraft:diamond");
                defaultArray.add("minecraft:emerald");
                defaultArray.add("minecraft:quartz");
                defaultArray.add("minecraft:lapis_lazuli");
                defaultArray.add("minecraft:glowstone_dust");
                defaultArray.add("minecraft:blaze_powder");
                defaultArray.add("minecraft:ender_pearl");
                gson.toJson(defaultArray, writer);
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

    public static void saveIngotPlatePair(ResourceLocation ingotId, ResourceLocation plateId) {
        IngotPlatePair pairKey = new IngotPlatePair(ingotId, plateId);

        if (!registeredIngotPlatePairs.contains(pairKey)) {
            registeredIngotPlatePairs.add(pairKey);
            ingotToPlateMap.put(ingotId, plateId);
            plateToIngotMap.put(plateId, ingotId);

            JsonObject json = new JsonObject();
            JsonArray pairsArray = new JsonArray();

            for (IngotPlatePair pair : registeredIngotPlatePairs) {
                JsonObject pairObject = new JsonObject();
                pairObject.addProperty("ingot", pair.ingot().toString());
                pairObject.addProperty("plate", pair.plate().toString());
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

    // Method to get the plate from the given ingot
    public static ResourceLocation getPlateFromIngot(ResourceLocation ingotId) {
        return ingotToPlateMap.get(ingotId);
    }

    // Method to get the ingot from the given plate
    public static ResourceLocation getIngotFromPlate(ResourceLocation plateId) {
        return plateToIngotMap.get(plateId);
    }
}
