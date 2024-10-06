package com.portingdeadmods.moreplates.utils;

import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.config.MPConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ItemUtil {
    private static final Set<String> KNOWN_PLATES = new HashSet<>();
    private static final Set<String> KNOWN_GEARS = new HashSet<>();
    private static final Set<String> KNOWN_RODS = new HashSet<>();

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

    public static void registerGearIfNotYetDone(String ingotType, ResourceLocation ingotId, BiConsumer<ResourceLocation, Item> regCallback) {
        registerGearIfNotYetDone(ingotType, ingotId, regCallback, () -> new Item(new Item.Properties()));
    }

    public static void registerGearIfNotYetDone(String ingotType, ResourceLocation ingotId, BiConsumer<ResourceLocation, Item> regCallback, Supplier<Item> itemCreator) {
        if (!KNOWN_GEARS.add(ingotType)) {
            return;
        }

        ResourceLocation gearId = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, ingotType + "_gear");
        regCallback.accept(gearId, itemCreator.get());
        MPConfig.saveIngotGearPair(ingotId, gearId);
    }

    public static void registerRodIfNotYetDone(String ingotType, ResourceLocation ingotId, BiConsumer<ResourceLocation, Item> regCallback) {
        registerRodIfNotYetDone(ingotType, ingotId, regCallback, () -> new Item(new Item.Properties()));
    }

    public static void registerRodIfNotYetDone(String ingotType, ResourceLocation ingotId, BiConsumer<ResourceLocation, Item> regCallback, Supplier<Item> itemCreator) {
        if (!KNOWN_RODS.add(ingotType)) {
            return;
        }

        ResourceLocation rodId = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, ingotType + "_rod");
        regCallback.accept(rodId, itemCreator.get());
        MPConfig.saveIngotRodPair(ingotId, rodId);
    }
}
