package com.portingdeadmods.moreplates.datagen;

import com.google.gson.JsonObject;
import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.config.MPConfig;
import com.portingdeadmods.moreplates.utils.TextureUtils;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DynamicResourcePack {
    public static void init() {
        ClientAssetsGenerator generator = new ClientAssetsGenerator();
        generator.register();
    }

    public static class ClientAssetsGenerator extends DynClientResourcesGenerator {

        protected ClientAssetsGenerator() {
            super(new DynamicTexturePack(ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID,"generated_pack"), Pack.Position.TOP, false, false));
        }

        @Override
        public void regenerateDynamicAssets(ResourceManager manager) {
            MorePlatesMod.LOGGER.info("==========================================");
            MorePlatesMod.LOGGER.info("Starting dynamic asset generation process");
            MorePlatesMod.LOGGER.info("==========================================");

            BuiltInRegistries.ITEM.forEach((item) -> {
                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
                if (MorePlatesMod.MODID.equals(itemId.getNamespace())) {
                    String rawName = itemId.getPath();
                    if (rawName.contains("plate") || rawName.contains("gear") || rawName.contains("rod")) {
                        MorePlatesMod.LOGGER.info("----------------------------------------");
                        MorePlatesMod.LOGGER.info("Processing item: " + itemId);
                        ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, "item/" + rawName);
                        MorePlatesMod.LOGGER.info("Target texture location: " + textureLocation);

                        if (rawName.contains("plate")) {
                            processPlate(manager, itemId, rawName, textureLocation);
                        }

                        if (rawName.contains("gear")) {
                            processGear(manager, itemId, rawName, textureLocation);
                        }

                        if (rawName.contains("rod")) {
                            processRod(manager, itemId, rawName, textureLocation);
                        }

                        generateModel(rawName, itemId, textureLocation);
                    }
                }
            });

            MorePlatesMod.LOGGER.info("==========================================");
            MorePlatesMod.LOGGER.info("Finished dynamic asset generation process");
            MorePlatesMod.LOGGER.info("==========================================");
        }

        private void processPlate(ResourceManager manager, ResourceLocation itemId, String rawName, ResourceLocation textureLocation) {
            MorePlatesMod.LOGGER.info("## Processing Plate ##");
            ResourceLocation ingotId = MPConfig.getIngotFromPlate(itemId);
            MorePlatesMod.LOGGER.info("Plate config mapping for " + itemId + " -> " + ingotId);

            if (ingotId != null) {
                processTexture(manager, itemId, ingotId, "base_plate", textureLocation);
            } else {
                MorePlatesMod.LOGGER.warn("No ingot mapping found in config for plate: " + itemId);
            }
        }

        private void processGear(ResourceManager manager, ResourceLocation itemId, String rawName, ResourceLocation textureLocation) {
            MorePlatesMod.LOGGER.info("## Processing Gear ##");
            ResourceLocation ingotId = MPConfig.getIngotFromGear(itemId);
            MorePlatesMod.LOGGER.info("Gear config mapping for " + itemId + " -> " + ingotId);

            if (ingotId != null) {
                processTexture(manager, itemId, ingotId, "base_gear", textureLocation);
            } else {
                MorePlatesMod.LOGGER.warn("No ingot mapping found in config for gear: " + itemId);
            }
        }

        private void processRod(ResourceManager manager, ResourceLocation itemId, String rawName, ResourceLocation textureLocation) {
            MorePlatesMod.LOGGER.info("## Processing Rod ##");
            ResourceLocation ingotId = MPConfig.getIngotFromRod(itemId);
            MorePlatesMod.LOGGER.info("Rod config mapping for " + itemId + " -> " + ingotId);

            if (ingotId != null) {
                processTexture(manager, itemId, ingotId, "base_rod", textureLocation);
            } else {
                MorePlatesMod.LOGGER.warn("No ingot mapping found in config for rod: " + itemId);
            }
        }

        private void processTexture(ResourceManager manager, ResourceLocation itemId, ResourceLocation ingotId, String baseTextureName, ResourceLocation textureLocation) {
            ResourceLocation ingotTexture = ResourceLocation.fromNamespaceAndPath(ingotId.getNamespace(), "item/" + ingotId.getPath());
            ResourceLocation baseTexture = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, "item/" + baseTextureName);

            MorePlatesMod.LOGGER.info("Texture paths:");
            MorePlatesMod.LOGGER.info("- Base texture: " + baseTexture);
            MorePlatesMod.LOGGER.info("- Ingot texture: " + ingotTexture);

            ResourceLocation baseTextureFullPath = baseTexture.withPrefix("textures/").withSuffix(".png");
            boolean baseExists = manager.getResource(baseTextureFullPath).isPresent();
            MorePlatesMod.LOGGER.info("Base texture exists: " + baseExists);
            if (!baseExists) {
                MorePlatesMod.LOGGER.error("Missing base texture at: " + baseTextureFullPath);
            }

            ResourceLocation ingotTextureFullPath = ingotTexture.withPrefix("textures/").withSuffix(".png");
            boolean ingotExists = manager.getResource(ingotTextureFullPath).isPresent();
            MorePlatesMod.LOGGER.info("Ingot texture exists: " + ingotExists);
            if (!ingotExists) {
                MorePlatesMod.LOGGER.error("Missing ingot texture at: " + ingotTextureFullPath);
            }

            if (baseExists && ingotExists) {
                try {
                    MorePlatesMod.LOGGER.info("Attempting to generate recolored texture");
                    TextureImage newTexture = TextureUtils.createRecoloredTexture(manager, baseTexture, ingotTexture);
                    this.dynamicPack.addAndCloseTexture(textureLocation, newTexture);
                    MorePlatesMod.LOGGER.info("Successfully generated and added texture: " + textureLocation);
                } catch (Exception e) {
                    MorePlatesMod.LOGGER.error("Failed to generate texture for " + itemId);
                    MorePlatesMod.LOGGER.error("Error details: " + e.getMessage());
                    if (e.getCause() != null) {
                        MorePlatesMod.LOGGER.error("Caused by: " + e.getCause().getMessage());
                    }
                    e.printStackTrace();
                }
            } else {
                MorePlatesMod.LOGGER.error("Skipping texture generation due to missing resources");
            }
        }

        private void generateModel(String rawName, ResourceLocation itemId, ResourceLocation textureLocation) {
            MorePlatesMod.LOGGER.info("## Generating Model ##");

            JsonObject model = new JsonObject();
            model.addProperty("parent", "item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", textureLocation.toString());

            MorePlatesMod.LOGGER.info("Base model layer: " + textureLocation.toString());

            if (rawName.contains("double") && !rawName.contains("hot")) {
                String doubleTexture = MorePlatesMod.MODID + ":item/double_sign";
                textures.addProperty("layer1", doubleTexture);
                MorePlatesMod.LOGGER.info("Added double layer texture: " + doubleTexture);
            }

            if (rawName.contains("hot") && !rawName.contains("double")) {
                String hotSignTexture = MorePlatesMod.MODID + ":item/hot_sign";
                textures.addProperty("layer1", hotSignTexture);
                MorePlatesMod.LOGGER.info("Added hot layer texture: " + hotSignTexture);
            }

            model.add("textures", textures);
            this.dynamicPack.addItemModel(itemId, model);
            MorePlatesMod.LOGGER.info("Model generated and added for: " + itemId);
        }

        @Override
        public void addDynamicTranslations(AfterLanguageLoadEvent languageEvent) {
            BuiltInRegistries.ITEM.forEach((item) -> {
                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
                if (MorePlatesMod.MODID.equals(itemId.getNamespace())) {
                    String rawName = itemId.getPath();

                    String languageKey = "item." + MorePlatesMod.MODID + '.' + rawName;
                    String formattedName = Stream.of(rawName.split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));

                    languageEvent.addEntry(languageKey, formattedName);
                }
            });
        }

        @Override
        public Logger getLogger() {
            return MorePlatesMod.LOGGER;
        }

        @Override
        public boolean dependsOnLoadedPacks() {
            return true;
        }
    }
}
