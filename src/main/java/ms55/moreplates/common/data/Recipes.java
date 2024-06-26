package ms55.moreplates.common.data;

import java.util.function.Consumer;

import blusunrize.immersiveengineering.api.crafting.IngredientWithSize;
import blusunrize.immersiveengineering.api.crafting.builders.MetalPressRecipeBuilder;
import blusunrize.immersiveengineering.common.items.IEItems;
import ms55.moreplates.MorePlates;
import ms55.moreplates.client.config.Config;
import ms55.moreplates.common.advancements.BooleanCondition;
import ms55.moreplates.common.enumeration.EnumMaterials;
import ms55.moreplates.common.util.Mods;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class Recipes extends RecipeProvider implements IConditionBuilder {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
		Item HAMMER = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MorePlates.MODID, "hammer"));

    	ConditionalRecipe.builder()
        .addCondition(
        	and(
                itemExists(MorePlates.MODID, "hammer")
            )
        )
        .addRecipe(
        	ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, MorePlates.HAMMER)
                .patternLine("IGI")
                .patternLine(" S ")
                .patternLine(" S ")
                .key('I', Items.IRON_INGOT)
                .key('G', Items.GOLD_INGOT)
                .key('S', MoreTags.Items.createTag("rods", "wooden"))
                .setGroup("")
                .addCriterion("has_item", hasItem(Items.IRON_INGOT))
                ::build
        )
        .build(consumer, new ResourceLocation(MorePlates.MODID, "hammer"));

    	for (EnumMaterials material : EnumMaterials.values()) {
    		int limit = 3;

    		if (material.equals(EnumMaterials.WOOD)) {
    			limit = 2;
    		}

    		System.out.println(material.getName());

    		for (int i = 0; i < limit; i++) {
    			if (i == 0) {
    				Item PLATE = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MorePlates.MODID, material.toString() + "_plate"));
    				ITag<Item> TAG = ItemTags.createOptional(material.getTag());
    				Ingredient INGOT = Ingredient.fromTag(TAG);

    				ConditionalRecipe.builder()
                    .addCondition(
                    	and(
                            new BooleanCondition(() -> Config.GENERAL.PLATE_RECIPES.get(), BooleanCondition.Type.ENABLE_PLATE.get()),
                            itemExists(MorePlates.MODID, material.toString() + "_plate")
                        )
                    )
                    .addRecipe(
                    	ShapedRecipeBuilder.shapedRecipe(PLATE)
                            .patternLine("H")
                            .patternLine("I")
                            .patternLine("I")
                            .key('H', HAMMER)
                            .key('I', INGOT)
                            .setGroup("")
        	                .addCriterion("has_item", hasItem(HAMMER))
                            ::build
                    )
                    .build(consumer, new ResourceLocation(MorePlates.MODID, material.toString() + "_plate"));

    				if (Mods.IMMERSIVE_ENGINEERING.isModPresent()) {
    					MetalPressRecipeBuilder.builder(IEItems.Molds.moldPlate, PLATE)
    						.addCondition(
    	                    	and(
    	                            new BooleanCondition(() -> Config.GENERAL.PLATE_RECIPES.get(), BooleanCondition.Type.ENABLE_PLATE.get()),
    	                            itemExists(MorePlates.MODID, material.toString() + "_plate")
    	                        )
    	                    )
    						.addInput(INGOT)
    						.setEnergy(2400)
    					    .build(consumer, new ResourceLocation(MorePlates.MODID, "metal_press/" + material.toString() + "_plate"));
    				}
    			} else if (i == 1) {
    				Item GEAR = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MorePlates.MODID, material.toString() + "_gear"));
    				ITag<Item> TAG = ItemTags.createOptional(material.getTag());
    				Ingredient INGOT = Ingredient.fromTag(TAG);

    				if (material.toString().equalsIgnoreCase("wood")) {
    					System.out.println("woodengear");
        				ConditionalRecipe.builder()
                        .addCondition(
                        	and(
                        		new BooleanCondition(() -> Config.GENERAL.GEAR_RECIPES.get(), BooleanCondition.Type.ENABLE_GEAR.get()),
                                itemExists(MorePlates.MODID, material.toString() + "_gear")
                        	)
                        )
                        .addRecipe(
                        	ShapedRecipeBuilder.shapedRecipe(GEAR)
                            	.patternLine(" x ")
                            	.patternLine("x x")
                            	.patternLine(" x ")
                            	.key('x', INGOT)
            	                .setGroup("")
            	                .addCriterion("has_item", hasItem(Blocks.OAK_WOOD))
            	                ::build
                        )
                        .build(consumer, new ResourceLocation(MorePlates.MODID, material.toString() + "_gear"));
    				} else {
        				ConditionalRecipe.builder()
                        .addCondition(
                        	and(
                        		new BooleanCondition(() -> Config.GENERAL.GEAR_RECIPES.get(), BooleanCondition.Type.ENABLE_GEAR.get()),
                                itemExists(MorePlates.MODID, material.toString() + "_gear")
                        	)
                        )
                        .addRecipe(
                        	ShapedRecipeBuilder.shapedRecipe(GEAR)
                            	.patternLine(" x ")
                            	.patternLine("x#x")
                            	.patternLine(" x ")
                            	.key('x', INGOT)
                            	.key('#', Tags.Items.INGOTS_IRON)
            	                .setGroup("")
            	                .addCriterion("has_item", hasItem(Items.IRON_INGOT))
            	                ::build
                        )
                        .build(consumer, new ResourceLocation(MorePlates.MODID, material.toString() + "_gear"));
    				}

    				if (Mods.IMMERSIVE_ENGINEERING.isModPresent()) {
    					MetalPressRecipeBuilder.builder(IEItems.Molds.moldGear, GEAR)
    						.addCondition(
    	                    	and(
    	                    		new BooleanCondition(() -> Config.GENERAL.GEAR_RECIPES.get(), BooleanCondition.Type.ENABLE_GEAR.get()),
    	                            itemExists(MorePlates.MODID, material.toString() + "_gear")
    	                    	)
    	                    )
							.addInput(new IngredientWithSize(TAG, 4))
							.setEnergy(2400)
							.build(consumer, new ResourceLocation(MorePlates.MODID, "metal_press/" + material.toString() + "_gear"));
    				}
    			} else {
    				Item STICK = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MorePlates.MODID, material.toString() + "_stick"));
    				ITag<Item> TAG = ItemTags.createOptional(material.getTag());
    				Ingredient INGOT = Ingredient.fromTag(TAG);

    				ConditionalRecipe.builder()
                    .addCondition(
                    	and(
                    		new BooleanCondition(() -> Config.GENERAL.ROD_RECIPES.get(), BooleanCondition.Type.ENABLE_STICK.get()),
                            itemExists(MorePlates.MODID, material.toString() + "_stick")
                    	)
                    )
                    .addRecipe(
                    	ShapedRecipeBuilder.shapedRecipe(STICK, 1)
                        	.patternLine("x")
                        	.patternLine("x")
                        	.key('x', INGOT)
        	                .setGroup("")
        	                .addCriterion("has_item", hasItem(Items.GOLD_INGOT))
        	                ::build
                    )
                    .build(consumer, new ResourceLocation(MorePlates.MODID, material.toString() + "_stick"));

    				if (Mods.IMMERSIVE_ENGINEERING.isModPresent()) {
    					MetalPressRecipeBuilder.builder(IEItems.Molds.moldRod, new ItemStack(STICK, 2))
    						.addCondition(
    	                    	and(
    	                    		new BooleanCondition(() -> Config.GENERAL.ROD_RECIPES.get(), BooleanCondition.Type.ENABLE_STICK.get()),
    	                            itemExists(MorePlates.MODID, material.toString() + "_stick")
    	                    	)
    	                    )
							.addInput(new IngredientWithSize(TAG, 1))
							.setEnergy(2400)
						.build(consumer, new ResourceLocation(MorePlates.MODID, "metal_press/" + material.toString() + "_stick"));
    				}
    			}
    		}
		}
    }
}