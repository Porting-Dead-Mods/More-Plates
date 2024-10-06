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
    private record IngotGearPair(ResourceLocation ingot, ResourceLocation gear) {}
    private record IngotRodPair(ResourceLocation ingot, ResourceLocation rod) {}

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = new File("config/moreplates/ingot_plate_pairs.json");
    private static final File ingotGearConfigFile = new File("config/moreplates/ingot_gear_pairs.json");
    private static final File ingotRodConfigFile = new File("config/moreplates/ingot_rod_pairs.json");
    private static final File generatorConfigFile = new File("config/moreplates/moreplates-generator.json");
    private static final List<String> generatorValues = new ArrayList<>();
    private static final Set<IngotPlatePair> registeredIngotPlatePairs = new HashSet<>();
    private static final Set<IngotGearPair> registeredIngotGearPairs = new HashSet<>();
    private static final Set<IngotRodPair> registeredIngotRodPairs = new HashSet<>();
    private static final Map<ResourceLocation, ResourceLocation> ingotToPlateMap = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> plateToIngotMap = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> ingotToGearMap = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> gearToIngotMap = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> ingotToRodMap = new HashMap<>();
    private static final Map<ResourceLocation, ResourceLocation> rodToIngotMap = new HashMap<>();

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

    public static void loadIngotGearConfig() {
        FMLLoader.getGamePath().resolve("config").resolve(MorePlatesMod.MODID).toFile().mkdirs();
        if (ingotGearConfigFile.exists()) {
            try (FileReader reader = new FileReader(ingotGearConfigFile)) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonArray pairs = json.getAsJsonArray("ingot_gear_pairs");

                if (pairs != null) {
                    for (var entry : pairs) {
                        JsonObject pair = entry.getAsJsonObject();
                        ResourceLocation ingot = ResourceLocation.parse(pair.get("ingot").getAsString());
                        ResourceLocation gear = ResourceLocation.parse(pair.get("gear").getAsString());
                        registeredIngotGearPairs.add(new IngotGearPair(ingot, gear));
                        ingotToGearMap.put(ingot, gear);
                        gearToIngotMap.put(gear, ingot);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadIngotRodConfig() {
        FMLLoader.getGamePath().resolve("config").resolve(MorePlatesMod.MODID).toFile().mkdirs();
        if (ingotRodConfigFile.exists()) {
            try (FileReader reader = new FileReader(ingotRodConfigFile)) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonArray pairs = json.getAsJsonArray("ingot_rod_pairs");

                if (pairs != null) {
                    for (var entry : pairs) {
                        JsonObject pair = entry.getAsJsonObject();
                        ResourceLocation ingot = ResourceLocation.parse(pair.get("ingot").getAsString());
                        ResourceLocation rod = ResourceLocation.parse(pair.get("rod").getAsString());
                        registeredIngotRodPairs.add(new IngotRodPair(ingot, rod));
                        ingotToRodMap.put(ingot, rod);
                        rodToIngotMap.put(rod, ingot);
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

    public static void saveIngotGearPair(ResourceLocation ingotId, ResourceLocation gearId) {
        IngotGearPair pairKey = new IngotGearPair(ingotId, gearId);

        if (!registeredIngotGearPairs.contains(pairKey)) {
            registeredIngotGearPairs.add(pairKey);
            ingotToGearMap.put(ingotId, gearId);
            gearToIngotMap.put(gearId, ingotId);

            JsonObject json = new JsonObject();
            JsonArray pairsArray = new JsonArray();

            for (IngotGearPair pair : registeredIngotGearPairs) {
                JsonObject pairObject = new JsonObject();
                pairObject.addProperty("ingot", pair.ingot().toString());
                pairObject.addProperty("gear", pair.gear().toString());
                pairsArray.add(pairObject);
            }

            json.add("ingot_gear_pairs", pairsArray);

            try (FileWriter writer = new FileWriter(ingotGearConfigFile)) {
                gson.toJson(json, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveIngotRodPair(ResourceLocation ingotId, ResourceLocation rodId) {
        IngotRodPair pairKey = new IngotRodPair(ingotId, rodId);

        if (!registeredIngotRodPairs.contains(pairKey)) {
            registeredIngotRodPairs.add(pairKey);
            ingotToRodMap.put(ingotId, rodId);
            rodToIngotMap.put(rodId, ingotId);

            JsonObject json = new JsonObject();
            JsonArray pairsArray = new JsonArray();

            for (IngotRodPair pair : registeredIngotRodPairs) {
                JsonObject pairObject = new JsonObject();
                pairObject.addProperty("ingot", pair.ingot().toString());
                pairObject.addProperty("rod", pair.rod().toString());
                pairsArray.add(pairObject);
            }

            json.add("ingot_rod_pairs", pairsArray);

            try (FileWriter writer = new FileWriter(ingotRodConfigFile)) {
                gson.toJson(json, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getGeneratorValues() {
        return generatorValues;
    }

    public static ResourceLocation getPlateFromIngot(ResourceLocation ingotId) {
        return ingotToPlateMap.get(ingotId);
    }

    public static ResourceLocation getIngotFromPlate(ResourceLocation plateId) {
        return plateToIngotMap.get(plateId);
    }

    public static ResourceLocation getGearFromIngot(ResourceLocation ingotId) {
        return ingotToGearMap.get(ingotId);
    }

    public static ResourceLocation getIngotFromGear(ResourceLocation gearId) {
        return gearToIngotMap.get(gearId);
    }

    public static ResourceLocation getRodFromIngot(ResourceLocation ingotId) {
        return ingotToRodMap.get(ingotId);
    }

    public static ResourceLocation getIngotFromRod(ResourceLocation rodId) {
        return rodToIngotMap.get(rodId);
    }
}
