package com.portingdeadmods.moreplates.datagen;

import com.google.gson.JsonObject;
import com.portingdeadmods.moreplates.MorePlatesMod;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;

public class DynamicPack {
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
            BuiltInRegistries.ITEM.forEach((item) -> {
                if (item.getDescriptionId().contains(MorePlatesMod.MODID)) {
                    String rawName = item.getDescriptionId()
                            .replace("item.", "")
                            .replace(MorePlatesMod.MODID + ":", "")
                            .replace("moreplates.", "");

                    String texture = MorePlatesMod.MODID + ":item/" + rawName;

                    JsonObject model = new JsonObject();
                    model.addProperty("parent", "item/generated");
                    JsonObject textures = new JsonObject();
                    textures.addProperty("layer0", texture);
                    model.add("textures", textures);

                    ResourceLocation modelLocation = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, rawName);

                    this.dynamicPack.addItemModel(modelLocation, model);

                    System.out.println("Generated model for " + modelLocation + ": " + model);
                }
            });
        }

        @Override
        public void addDynamicTranslations(AfterLanguageLoadEvent languageEvent) {
            BuiltInRegistries.ITEM.forEach((item) -> {
                if (item.getDescriptionId().contains(MorePlatesMod.MODID)) {
                    String rawName = item.getDescriptionId().replace("item.", "").replace(MorePlatesMod.MODID + ":", "");

                    String[] words = rawName.replace("moreplates.", "").split("_");
                    StringBuilder formattedName = new StringBuilder();

                    for (String word : words) {
                        formattedName.append(Character.toUpperCase(word.charAt(0)))
                                .append(word.substring(1)).append(" ");
                    }

                    String languageKey = "item." + rawName;
                    String formattedDisplayName = formattedName.toString().trim();

                    languageEvent.addEntry(languageKey, formattedDisplayName);
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
