package com.portingdeadmods.moreplates.mixins;

import com.portingdeadmods.moreplates.MorePlatesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(DeferredRegister.class)
public abstract class DeferredRegisterMixin<T> {

    @Shadow @Final private Map<DeferredHolder<T, ? extends T>, Supplier<? extends T>> entries;

    @Inject(method = "addEntries", at = @At("TAIL"))
    private void inject(RegisterEvent event, CallbackInfo ci) {
        for (Map.Entry<DeferredHolder<T, ? extends T>, Supplier<? extends T>> entry : entries.entrySet()) {
            ResourceLocation registryName = entry.getKey().getKey().registry();
            ResourceLocation id = entry.getKey().getId();

            more_Plates$onRegistryObjectCreated(registryName, id, event);
        }
    }

    @Unique
    private static void more_Plates$onRegistryObjectCreated(ResourceLocation registryName, ResourceLocation id, RegisterEvent event) {
        if (registryName.getPath().contains("item") && id.getPath().contains("ingot")) {
            String ingotType = id.getPath().replace("ingot", "");
            ResourceLocation plateId = ResourceLocation.fromNamespaceAndPath(MorePlatesMod.MODID, ingotType + "_plate");
            event.register(Registries.ITEM, plateId, () -> new Item(new Item.Properties()));
        }
    }
}
