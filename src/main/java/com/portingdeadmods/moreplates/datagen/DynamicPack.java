package com.portingdeadmods.moreplates.datagen;

import com.google.gson.JsonObject;
import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.registries.MPItems;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.mehvahdjukaar.moonlight.core.Moonlight;
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
            JsonObject model = new JsonObject();
            BuiltInRegistries.ITEM.forEach((item) -> {
                if(item.getDescriptionId().contains(MorePlatesMod.MODID)){
                    String rawName = item.getDescriptionId().replace("moreplates:", "");
                    String texture = MorePlatesMod.MODID + ":item/" + rawName;
                    model.addProperty("parent", "item/generated");
                    model.addProperty("layer0", texture);
                    this.dynamicPack.addItemModel(ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, "item/" + rawName), model);
                }
            });
        }

        @Override
        public void addDynamicTranslations(AfterLanguageLoadEvent languageEvent) {
            BuiltInRegistries.ITEM.forEach((item) -> {
                if(item.getDescriptionId().contains(MorePlatesMod.MODID)){
                    String rawName = item.getDescriptionId().replace("moreplates:", "");

                    String[] words = rawName.split("_");
                    StringBuilder formattedName = new StringBuilder();
                    for (String word : words) {
                        formattedName.append(Character.toUpperCase(word.charAt(0)))
                                .append(word.substring(1)).append(" ");
                    }

                    languageEvent.addEntry("item." + rawName, formattedName.toString().trim());
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
