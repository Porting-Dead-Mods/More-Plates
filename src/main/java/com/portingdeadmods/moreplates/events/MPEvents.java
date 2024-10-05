package com.portingdeadmods.moreplates.events;

import com.portingdeadmods.moreplates.MorePlatesMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = MorePlatesMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MPEvents {
    @SubscribeEvent
    public static void onRegisterItems(RegisterEvent event) {
        event.register(Registries.ITEM, helper -> {
            BuiltInRegistries.ITEM.stream().forEach(item -> {
                ResourceLocation itemID = BuiltInRegistries.ITEM.getKey(item);
                if (itemID.getPath().contains("ingot")) {
                    String plateName = itemID.getPath().replace("ingot", "plate");
                    helper.register(ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, plateName),
                            new Item(new Item.Properties()));
                }
            });
        });
    }
}