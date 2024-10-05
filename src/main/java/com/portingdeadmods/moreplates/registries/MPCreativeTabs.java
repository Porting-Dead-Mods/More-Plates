package com.portingdeadmods.moreplates.registries;

import com.portingdeadmods.moreplates.MorePlatesMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MPCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MorePlatesMod.MODID);

    public static final Supplier<CreativeModeTab> MORE_PLATES_TAB = CREATIVE_MODE_TABS.register("moreplatestab", () -> CreativeModeTab.builder()
            .title(Component.literal("More Plates"))
            .icon(() -> MPItems.INFINITY_PLATE.get().getDefaultInstance())
            .displayItems((params, output) -> {

            })
            .build());

}
