package com.portingdeadmods.moreplates.mixin;

import com.portingdeadmods.moreplates.registries.PlateGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(DeferredRegister.class)
public class DeferredRegisterMixin<T> {

    @Shadow @Final
    private Map<DeferredHolder<T, ? extends T>, Supplier<? extends T>> entries;

    // Inject at the start of the addEntries method to ensure we are in the correct phase
    @Inject(method = "addEntries", at = @At("HEAD"))
    private void onAddEntries(RegisterEvent event, CallbackInfo ci) {
        for (Map.Entry<DeferredHolder<T, ? extends T>, Supplier<? extends T>> entry : entries.entrySet()) {
            PlateGenerator.generatePlateForIngot(entry.getKey().getKey().registry(), entry.getKey().getId(), event);
        }
    }
}



