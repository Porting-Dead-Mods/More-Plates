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
                if (item.getDescriptionId().contains(MorePlatesMod.MODID) && item.getDescriptionId().contains("plate")) {
                    String rawName = item.getDescriptionId()
                            .replace("item.", "")
                            .replace(MorePlatesMod.MODID + ":", "")
                            .replace("moreplates.", "");

                    ResourceLocation modelLocation = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, rawName);
                    ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, "item/" + rawName);
                    String texture = MorePlatesMod.MODID + ":item/" + rawName;
                    String doubleTexture = MorePlatesMod.MODID + ":item/double_sign";
                    String hotSignTexture = MorePlatesMod.MODID + ":item/hot_sign";

                    String ingotId = MPConfig.getIngotFromPlate(MorePlatesMod.MODID + ":" + rawName);
                    if(ingotId != null){
                        String[] parts = ingotId.split(":", 2);
                        String itemNamespace = parts[0];
                        String itemId = parts[1];
                        ResourceLocation ingotTexture = ResourceLocation.fromNamespaceAndPath(itemNamespace, "item/" + itemId);
                        TextureImage newPlateTexture = TextureUtils.createRecoloredTexture(manager, ingotTexture);
                        this.dynamicPack.addAndCloseTexture(textureLocation, newPlateTexture);
                    }

                    JsonObject model = new JsonObject();
                    model.addProperty("parent", "item/generated");
                    JsonObject textures = new JsonObject();
                    textures.addProperty("layer0", texture);
                    if(rawName.contains("double") && !rawName.contains("hot")){
                        textures.addProperty("layer1", doubleTexture);
                    }
                    if(rawName.contains("hot") && !rawName.contains("double")){
                        textures.addProperty("layer1", hotSignTexture);
                    }
                    model.add("textures", textures);
                    //Recipes
                    this.dynamicPack.addItemModel(modelLocation, model);
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
