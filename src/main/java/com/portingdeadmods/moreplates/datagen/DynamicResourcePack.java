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
            BuiltInRegistries.ITEM.forEach((item) -> {
                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
                if (MorePlatesMod.MODID.equals(itemId.getNamespace()) && (itemId.getPath().contains("plate") || itemId.getPath().contains("gear") || itemId.getPath().contains("rod"))) {
                    String rawName = itemId.getPath();
                    ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, "item/" + rawName);

                    if (itemId.getPath().contains("plate")) {
                        ResourceLocation ingotId = MPConfig.getIngotFromPlate(itemId);
                        if (ingotId != null) {
                            ResourceLocation ingotTexture = ResourceLocation.fromNamespaceAndPath(ingotId.getNamespace(), "item/" + ingotId.getPath());
                            ResourceLocation plateTexture = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID,"item/base_plate");
                            TextureImage newPlateTexture = TextureUtils.createRecoloredTexture(manager, plateTexture, ingotTexture);
                            this.dynamicPack.addAndCloseTexture(textureLocation, newPlateTexture);
                        }
                    }

                    if (itemId.getPath().contains("gear")) {
                        ResourceLocation ingotId = MPConfig.getIngotFromGear(itemId);
                        if (ingotId != null) {
                            ResourceLocation ingotTexture = ResourceLocation.fromNamespaceAndPath(ingotId.getNamespace(), "item/" + ingotId.getPath());
                            ResourceLocation gearTexture = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID,"item/base_gear");
                            TextureImage newGearTexture = TextureUtils.createRecoloredTexture(manager, gearTexture, ingotTexture);
                            this.dynamicPack.addAndCloseTexture(textureLocation, newGearTexture);
                        }
                    }

                    if (itemId.getPath().contains("rod")) {
                        ResourceLocation ingotId = MPConfig.getIngotFromRod(itemId);
                        if (ingotId != null) {
                            ResourceLocation ingotTexture = ResourceLocation.fromNamespaceAndPath(ingotId.getNamespace(), "item/" + ingotId.getPath());
                            ResourceLocation rodTexture = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID,"item/base_rod");
                            TextureImage newRodTexture = TextureUtils.createRecoloredTexture(manager, rodTexture, ingotTexture);
                            this.dynamicPack.addAndCloseTexture(textureLocation, newRodTexture);
                        }
                    }

                    JsonObject model = new JsonObject();
                    model.addProperty("parent", "item/generated");
                    JsonObject textures = new JsonObject();
                    textures.addProperty("layer0", textureLocation.toString());

                    if (rawName.contains("double") && !rawName.contains("hot")) {
                        String doubleTexture = MorePlatesMod.MODID + ":item/double_sign";
                        textures.addProperty("layer1", doubleTexture);
                    }

                    if (rawName.contains("hot") && !rawName.contains("double")) {
                        String hotSignTexture = MorePlatesMod.MODID + ":item/hot_sign";
                        textures.addProperty("layer1", hotSignTexture);
                    }
                    model.add("textures", textures);
                    this.dynamicPack.addItemModel(itemId, model);
                }
            });
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
