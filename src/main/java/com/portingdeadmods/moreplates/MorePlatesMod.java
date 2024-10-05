package com.portingdeadmods.moreplates;

import com.portingdeadmods.moreplates.datagen.DynamicPack;
import com.portingdeadmods.moreplates.registries.MPCreativeTabs;
import com.portingdeadmods.moreplates.registries.MPItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MorePlatesMod.MODID)
public class MorePlatesMod
{
    public static final String MODID = "moreplates";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static Set<String> more_Plates$registeredPlates = new HashSet<>();

    public MorePlatesMod(IEventBus modEventBus, ModContainer modContainer) {
        MPCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        MPItems.ITEMS.register(modEventBus);
        DynamicPack.init();
        // modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}
