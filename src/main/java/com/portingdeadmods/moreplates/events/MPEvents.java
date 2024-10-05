package com.portingdeadmods.moreplates.events;

import com.portingdeadmods.moreplates.MorePlatesMod;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashSet;
import java.util.Set;


@EventBusSubscriber(modid = MorePlatesMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MPEvents {
    @SubscribeEvent
    public static void onRegisterItems(RegisterEvent event) {
        event.register(Registries.ITEM, helper -> {
            BuiltInRegistries.ITEM.stream().forEach(item -> {
                ResourceLocation itemID = BuiltInRegistries.ITEM.getKey(item);
                if (itemID.getPath().contains("ingot") && item.getDescriptionId().contains("minecraft")) {
                    String plateName = itemID.getPath().replace("ingot", "plate");
                    helper.register(ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, plateName),
                            new Item(new Item.Properties()));
                }
            });
        });
    }
}
