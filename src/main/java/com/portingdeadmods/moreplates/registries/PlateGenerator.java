package com.portingdeadmods.moreplates.registries;

import com.portingdeadmods.moreplates.MorePlatesMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

public class PlateGenerator {

    public static void generatePlateForIngot(ResourceLocation registryName,ResourceLocation id,RegisterEvent event) {
        if (isIngot(registryName) && false) {
            String ingotName = id.getPath();
            String plateName = ingotName.replace("ingot", "plate");

            event.register(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, plateName),
                    () -> new Item(new Item.Properties()));

            //MPItems.ITEMS.register(plateName, () -> new Item(new Item.Properties()));
        }
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == MPCreativeTabs.MAIN) {
            BuiltInRegistries.ITEM.stream()
                    .filter(rs -> rs.getDescriptionId().contains(MorePlatesMod.MODID))
                    .forEach(rs -> event.accept(rs.asItem()));
        }
    }

    private static boolean isIngot(ResourceLocation id) {
        return id.getPath().contains("_ingot");
    }
}
