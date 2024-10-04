package com.portingdeadmods.moreplates.content;

import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.registries.MPCreativeTabs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

public class PlateGenerator {

    public static void generatePlates(RegisterEvent event) {
        // Check that we are registering items
        if (event.getRegistryKey().equals(net.minecraft.core.registries.Registries.ITEM)) {
            BuiltInRegistries.ITEM.stream()
                    .filter(PlateGenerator::isIngot)  // Only process ingots
                    .forEach(ingot -> {
                        String ingotName = BuiltInRegistries.ITEM.getKey(ingot).getPath();
                        String plateName = ingotName.replace("ingot", "plate");

                        // Register the plate directly using the RegisterEvent
                        event.register(net.minecraft.core.registries.Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, plateName),
                                () -> new Item(new Item.Properties()));
                    });
        }
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == MPCreativeTabs.MAIN) {
            BuiltInRegistries.ITEM.stream()
                    .filter(rs -> rs.getDescriptionId().contains(MorePlatesMod.MODID))
                    .forEach(rs -> event.accept(rs.asItem()));
        }
    }

    private static boolean isIngot(Item item) {
        return item.getDescriptionId().contains("ingot");
    }
}
