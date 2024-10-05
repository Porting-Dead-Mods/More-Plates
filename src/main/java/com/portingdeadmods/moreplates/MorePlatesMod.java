package com.portingdeadmods.moreplates;

import com.portingdeadmods.moreplates.datagen.DynamicPack;
import com.portingdeadmods.moreplates.registries.MPCreativeTabs;
import com.portingdeadmods.moreplates.registries.MPItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(MorePlatesMod.MODID)
public class MorePlatesMod
{
    public static final String MODID = "moreplates";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public MorePlatesMod(IEventBus modEventBus, ModContainer modContainer) {
        MPCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        MPItems.ITEMS.register(modEventBus);
        DynamicPack.init();
    }

}
