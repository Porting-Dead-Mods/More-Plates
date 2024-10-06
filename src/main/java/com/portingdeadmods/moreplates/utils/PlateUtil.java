package com.portingdeadmods.moreplates.utils;

import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.config.MPConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PlateUtil {
    private static final Set<String> KNOWN_PLATES = new HashSet<>();

    public static void registerPlateIfNotYetDone(String ingotType, ResourceLocation ingotId, BiConsumer<ResourceLocation, Item> regCallback) {
        registerPlateIfNotYetDone(ingotType, ingotId, regCallback, () -> new Item(new Item.Properties()));
    }

    public static void registerPlateIfNotYetDone(String ingotType, ResourceLocation ingotId, BiConsumer<ResourceLocation, Item> regCallback, Supplier<Item> itemCreator) {
        if (!KNOWN_PLATES.add(ingotType)) {
            return;
        }

        ResourceLocation plateId = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, ingotType + "_plate");
        regCallback.accept(plateId, itemCreator.get());
        MPConfig.saveIngotPlatePair(ingotId, plateId);
    }
}
