package com.portingdeadmods.moreplates.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.config.MPConfig;
import com.portingdeadmods.moreplates.registries.MPItems;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicDataPack extends DynServerResourcesGenerator {

    public static final DynamicDataPack INSTANCE = new DynamicDataPack();

    public DynamicDataPack() {
        super(new net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack(ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID,"generated_pack"), Pack.Position.TOP, false, false));
        this.dynamicPack.setGenerateDebugResources(PlatHelper.isDev());
    }



    @Override
    public Logger getLogger() {
        return MorePlatesMod.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {
        BuiltInRegistries.ITEM.forEach((item) -> {
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
            if (MorePlatesMod.MODID.equals(itemId.getNamespace()) && itemId.getPath().contains("plate")) {
                // Get the raw item name without prefixes
                String rawName = itemId.getPath();

                ResourceLocation inputIngot = MPConfig.getIngotFromPlate(itemId);
                if(inputIngot == null) return;

                Item inputItem = BuiltInRegistries.ITEM.get(inputIngot);
                ShapedRecipeBuilder recipeBuilder = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item)
                        .pattern("AB")
                        .pattern("B ")
                        .define('A', MPItems.HAMMER)
                        .define('B', inputItem)
                        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(inputItem));

                recipeBuilder.save(new RecipeOutput() {
                    @Override
                    public Advancement.@NotNull Builder advancement() {
                        return Advancement.Builder.advancement();
                    }

                    @Override
                    public void accept(ResourceLocation resourceLocation, Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition... iConditions) {
                        dynamicPack.addRecipe(recipe, resourceLocation);
                    }
                });

            }
        });
    }


    private static void removeNullEntries(JsonObject jsonObject) {
        jsonObject.entrySet().removeIf(entry -> entry.getValue().isJsonNull());

        jsonObject.entrySet().forEach(entry -> {
            JsonElement element = entry.getValue();
            if (element.isJsonObject()) {
                removeNullEntries(element.getAsJsonObject());
            } else if (element.isJsonArray()) {
                removeNullEntries(element.getAsJsonArray());
            }
        });
    }

    private static void removeNullEntries(JsonArray jsonArray) {
        JsonArray newArray = new JsonArray();
        jsonArray.forEach(element -> {
            if (!element.isJsonNull()) {
                if (element.isJsonObject()) {
                    removeNullEntries(element.getAsJsonObject().getAsJsonArray());
                } else if (element.isJsonArray()) {
                    removeNullEntries(element.getAsJsonArray());
                }
                newArray.add(element);
            }
        });
        for (int i = 0; i < jsonArray.size(); i++) jsonArray.remove(i);
        jsonArray.addAll(newArray);
    }
}
