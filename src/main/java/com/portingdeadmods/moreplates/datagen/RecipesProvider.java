package com.portingdeadmods.moreplates.datagen;

import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.config.MPConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class RecipesProvider extends RecipeProvider {
    public RecipesProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {
        BuiltInRegistries.ITEM.forEach((item) -> {
            if (item.getDescriptionId().contains(MorePlatesMod.MODID)) {
                String rawName = item.getDescriptionId()
                        .replace("item.", "")
                        .replace(MorePlatesMod.MODID + ":", "")
                        .replace("moreplates.", "");

                String fullNameOutput = MorePlatesMod.MODID+":"+rawName;
                String fullNameInput = MPConfig.getIngotFromPlate(fullNameOutput);
                if(fullNameInput == null) return;
                String namespace = fullNameInput.split(":")[0];
                String path = fullNameInput.split(":")[1];

                ItemStack inputItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(namespace,path)).getDefaultInstance();
                ItemStack outputItem = item.getDefaultInstance();

                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputItem)
                        .pattern("AB ")
                        .pattern("A  ")
                        .define('A', Items.STICK)
                        .define('B', inputItem.getItem())
                        .unlockedBy("has_item", has(inputItem.getItem()))
                        .save(pRecipeOutput);
            }
        });
    }
}