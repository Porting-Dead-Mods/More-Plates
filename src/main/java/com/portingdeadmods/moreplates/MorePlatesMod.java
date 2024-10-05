package com.portingdeadmods.moreplates;

import com.portingdeadmods.moreplates.registries.PlateGenerator;
import com.portingdeadmods.moreplates.registries.MPCreativeTabs;
import com.portingdeadmods.moreplates.registries.MPItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MorePlatesMod.MODID)
public class MorePlatesMod
{
    public static final String MODID = "moreplates";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MorePlatesMod(IEventBus modEventBus, ModContainer modContainer) {
        MPCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        MPItems.ITEMS.register(modEventBus);
        // modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modEventBus.addListener(PlateGenerator::addCreative);
    }


}
