package com.portingdeadmods.moreplates.datagen.compat;
import aztech.modern_industrialization.MIFluids;
import aztech.modern_industrialization.machines.init.MIMachineRecipeTypes;
import aztech.modern_industrialization.machines.recipe.MachineRecipeBuilder;
import aztech.modern_industrialization.machines.recipe.MachineRecipeType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.config.MPConfig;
import com.portingdeadmods.moreplates.datagen.DynamicDataPack;
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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MIDynamicDataPack extends DynServerResourcesGenerator {

    public static final MIDynamicDataPack INSTANCE = new MIDynamicDataPack();

    public MIDynamicDataPack() {
        super(new net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack(ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID,"mi_generated_pack"), Pack.Position.TOP, false, false));
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

            // Plates
            if (MorePlatesMod.MODID.equals(itemId.getNamespace()) && itemId.getPath().contains("plate")) {
                String rawName = itemId.getPath();
                ResourceLocation inputIngot = MPConfig.getIngotFromPlate(itemId);
                if (inputIngot == null) return;

                Item inputItem = BuiltInRegistries.ITEM.get(inputIngot);

                MachineRecipeBuilder recipeBuilder = new MachineRecipeBuilder(MIMachineRecipeTypes.COMPRESSOR, 2, 400)
                        .addItemInput((ItemLike) inputItem,1)
                        .addItemOutput(item, 1);

                recipeBuilder.offerTo(new RecipeOutput() {
                    @Override
                    public Advancement.@NotNull Builder advancement() {
                        return Advancement.Builder.advancement();
                    }

                    @Override
                    public void accept(@NotNull ResourceLocation resourceLocation, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition... iConditions) {
                        //Change namespace from modern_industrialization to moreplates
                        resourceLocation = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, resourceLocation.getPath());
                        dynamicPack.addRecipe(recipe, resourceLocation);
                    }
                }, "compressor/"+itemId.getPath());
            }

            // Gears
            if (MorePlatesMod.MODID.equals(itemId.getNamespace()) && itemId.getPath().contains("gear")) {
                String rawName = itemId.getPath();
                ResourceLocation inputIngot = MPConfig.getIngotFromGear(itemId);
                if (inputIngot == null) return;

                Item inputItem = BuiltInRegistries.ITEM.get(inputIngot);

                MachineRecipeBuilder recipeBuilder = new MachineRecipeBuilder(MIMachineRecipeTypes.ASSEMBLER, 2, 200)
                        .addItemInput((ItemLike) inputItem,4)
                        .addFluidInput(MIFluids.SOLDERING_ALLOY, 100)
                        .addItemOutput(item, 2);

                recipeBuilder.offerTo(new RecipeOutput() {
                    @Override
                    public Advancement.@NotNull Builder advancement() {
                        return Advancement.Builder.advancement();
                    }

                    @Override
                    public void accept(@NotNull ResourceLocation resourceLocation, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition... iConditions) {
                        //Change namespace from modern_industrialization to moreplates
                        resourceLocation = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, resourceLocation.getPath());
                        dynamicPack.addRecipe(recipe, resourceLocation);
                    }
                }, "assembler/"+itemId.getPath());
            }

            // Rods
            if (MorePlatesMod.MODID.equals(itemId.getNamespace()) && itemId.getPath().contains("rod")) {
                String rawName = itemId.getPath();
                ResourceLocation inputIngot = MPConfig.getIngotFromRod(itemId);
                if (inputIngot == null) return;

                Item inputItem = BuiltInRegistries.ITEM.get(inputIngot);

                MachineRecipeBuilder recipeBuilder = new MachineRecipeBuilder(MIMachineRecipeTypes.CUTTING_MACHINE, 2, 200)
                        .addItemInput((ItemLike) inputItem,2)
                        .addFluidInput(MIFluids.LUBRICANT, 1)
                        .addItemOutput(item, 2);

                recipeBuilder.offerTo(new RecipeOutput() {
                    @Override
                    public Advancement.@NotNull Builder advancement() {
                        return Advancement.Builder.advancement();
                    }

                    @Override
                    public void accept(@NotNull ResourceLocation resourceLocation, @NotNull Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, ICondition... iConditions) {
                        //Change namespace from modern_industrialization to moreplates
                        resourceLocation = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, resourceLocation.getPath());
                        dynamicPack.addRecipe(recipe, resourceLocation);
                    }
                }, "cutting_machine/"+itemId.getPath());

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

