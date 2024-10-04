package com.portingdeadmods.moreplates.registries;

import com.portingdeadmods.moreplates.MorePlatesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MPCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MorePlatesMod.MODID);

    public static final Supplier<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.literal("More Plates"))
            .icon(Items.IRON_BLOCK::getDefaultInstance)
            .displayItems((params, output) -> {

            })
            .build());

}
