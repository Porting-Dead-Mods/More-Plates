package com.portingdeadmods.moreplates.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Decoder;
import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.config.MPConfig;
import com.portingdeadmods.moreplates.registries.MPItems;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
        this.dynamicPack.addNamespaces("c");
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

            // Plates
            if (MorePlatesMod.MODID.equals(itemId.getNamespace()) && itemId.getPath().contains("plate")) {
                String rawName = itemId.getPath();
                ResourceLocation inputIngot = MPConfig.getIngotFromPlate(itemId);
                if (inputIngot == null) return;

                Item inputItem = BuiltInRegistries.ITEM.get(inputIngot);

                SimpleTagBuilder tagBuilder = SimpleTagBuilder.of(ResourceLocation.fromNamespaceAndPath("c", "plates/" + rawName.replace("_plate", "")));
                SimpleTagBuilder generalTagBuilder = SimpleTagBuilder.of(ResourceLocation.fromNamespaceAndPath("c", "plates"));
                tagBuilder.addEntry(item);
                generalTagBuilder.addEntry(item);
                dynamicPack.addTag(tagBuilder, Registries.ITEM);
                dynamicPack.addTag(generalTagBuilder, Registries.ITEM);

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
                    public void accept(@NotNull ResourceLocation resourceLocation, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition... iConditions) {
                        dynamicPack.addRecipe(recipe, resourceLocation);
                    }
                });
            }

            // Gears
            if (MorePlatesMod.MODID.equals(itemId.getNamespace()) && itemId.getPath().contains("gear")) {
                String rawName = itemId.getPath();
                ResourceLocation inputIngot = MPConfig.getIngotFromGear(itemId);
                if (inputIngot == null) return;

                Item inputItem = BuiltInRegistries.ITEM.get(inputIngot);

                SimpleTagBuilder tagBuilder = SimpleTagBuilder.of(ResourceLocation.fromNamespaceAndPath("c", "gears/" + rawName.replace("_gear", "")));
                SimpleTagBuilder generalTagBuilder = SimpleTagBuilder.of(ResourceLocation.fromNamespaceAndPath("c", "gears"));
                tagBuilder.addEntry(item);
                generalTagBuilder.addEntry(item);
                dynamicPack.addTag(tagBuilder, Registries.ITEM);
                dynamicPack.addTag(generalTagBuilder, Registries.ITEM);

                ShapedRecipeBuilder recipeBuilder = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item)
                        .pattern(" I ")
                        .pattern("I I")
                        .pattern(" I ")
                        .define('I', inputItem)
                        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(inputItem));

                recipeBuilder.save(new RecipeOutput() {
                    @Override
                    public Advancement.@NotNull Builder advancement() {
                        return Advancement.Builder.advancement();
                    }

                    @Override
                    public void accept(@NotNull ResourceLocation resourceLocation, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition... iConditions) {
                        dynamicPack.addRecipe(recipe, resourceLocation);
                    }
                });
            }

            // Rods
            if (MorePlatesMod.MODID.equals(itemId.getNamespace()) && itemId.getPath().contains("rod")) {
                String rawName = itemId.getPath();
                ResourceLocation inputIngot = MPConfig.getIngotFromRod(itemId);
                if (inputIngot == null) return;

                Item inputItem = BuiltInRegistries.ITEM.get(inputIngot);

                SimpleTagBuilder tagBuilder = SimpleTagBuilder.of(ResourceLocation.fromNamespaceAndPath("c", "rods/" + rawName.replace("_rod", "")));
                SimpleTagBuilder generalTagBuilder = SimpleTagBuilder.of(ResourceLocation.fromNamespaceAndPath("c", "rods"));
                tagBuilder.addEntry(item);
                generalTagBuilder.addEntry(item);
                dynamicPack.addTag(tagBuilder, Registries.ITEM);
                dynamicPack.addTag(generalTagBuilder, Registries.ITEM);

                ShapedRecipeBuilder recipeBuilder = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item, 2)
                        .pattern("H")
                        .pattern("I")
                        .pattern("I")
                        .define('H', MPItems.HAMMER)
                        .define('I', inputItem)
                        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(inputItem));

                recipeBuilder.save(new RecipeOutput() {
                    @Override
                    public Advancement.@NotNull Builder advancement() {
                        return Advancement.Builder.advancement();
                    }

                    @Override
                    public void accept(@NotNull ResourceLocation resourceLocation, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition... iConditions) {
                        dynamicPack.addRecipe(recipe, resourceLocation);
                    }
                });
            }

            if(MorePlatesMod.MODID.equals(itemId.getNamespace())){
                if(itemId.getPath().contains("plate")){
                    ResourceLocation originalItem = MPConfig.getIngotFromPlate(itemId);
                    SimpleTagBuilder tagBuilder = SimpleTagBuilder.of(ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID,"mods/"+originalItem.getNamespace()));
                    tagBuilder.addEntry(item);
                    dynamicPack.addTag(tagBuilder, Registries.ITEM);
                }else if(itemId.getPath().contains("gear")){
                    ResourceLocation originalItem = MPConfig.getIngotFromGear(itemId);
                    SimpleTagBuilder tagBuilder = SimpleTagBuilder.of(ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID,"mods/"+originalItem.getNamespace()));
                    tagBuilder.addEntry(item);
                    dynamicPack.addTag(tagBuilder, Registries.ITEM);
                }else if(itemId.getPath().contains("rod")){
                    ResourceLocation originalItem = MPConfig.getIngotFromRod(itemId);
                    SimpleTagBuilder tagBuilder = SimpleTagBuilder.of(ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID,"mods/"+originalItem.getNamespace()));
                    tagBuilder.addEntry(item);
                    dynamicPack.addTag(tagBuilder, Registries.ITEM);
                }
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
