package com.portingdeadmods.moreplates.events;

import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.utils.IngotUtil;
import com.portingdeadmods.moreplates.utils.PlateUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.ModifyRegistriesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.callback.AddCallback;

import java.util.Set;
import java.util.function.BiConsumer;


@EventBusSubscriber(modid = MorePlatesMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MPEvents {
    private static class ItemRegistrationCallback implements AddCallback<Item> {
        @Override
        public void onAdd(Registry<Item> registry, int id, ResourceKey<Item> key, Item value) {
            registerPlateFor(key.location(), false, (plateId, plate) -> Registry.register(registry, plateId, plate));
        }
    }

    @SubscribeEvent
    public static void modifyRegistriesEvent(ModifyRegistriesEvent event) {
        event.getRegistry(Registries.ITEM).addCallback(new ItemRegistrationCallback());
    }

    @SubscribeEvent
    public static void onRegisterItems(RegisterEvent event) {
        event.register(Registries.ITEM, helper -> Set.copyOf(event.getRegistry().keySet()).forEach(key -> registerPlateFor(key, true, helper::register)));
    }

    private static void registerPlateFor(ResourceLocation id, boolean onlyVanilla, BiConsumer<ResourceLocation, Item> regCallback) {
        // Avoid accidental recursion when we register our own thing multiple times
        if (!MorePlatesMod.MODID.equals(id.getNamespace()) && IngotUtil.isValidIngot(id, onlyVanilla)) {
            String ingotType = IngotUtil.getIngotType(id);
            PlateUtil.registerPlateIfNotYetDone(ingotType, id, regCallback);
        }
    }
}
