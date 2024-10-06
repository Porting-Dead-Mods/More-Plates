package com.portingdeadmods.moreplates;

import com.portingdeadmods.moreplates.config.MPConfig;
import com.portingdeadmods.moreplates.datagen.DynamicDataPack;
import com.portingdeadmods.moreplates.datagen.DynamicPack;
import com.portingdeadmods.moreplates.registries.MPCreativeTabs;
import com.portingdeadmods.moreplates.registries.MPItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
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
        FMLLoader.getGamePath().resolve("config").resolve(MODID).toFile().mkdirs();
        MPConfig.loadGeneratorConfig();
        MPItems.registerCustomItems(MPConfig.getGeneratorValues());
        MPItems.ITEMS.register(modEventBus);
        MPCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(MorePlatesMod::onCreativeTab);
        DynamicPack.init();
        DynamicDataPack.INSTANCE.register();
    }

    public static void onCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if(event.getTab() == MPCreativeTabs.MORE_PLATES_TAB.get()){
            BuiltInRegistries.ITEM.stream()
                    .filter(rs -> rs.getDescriptionId().contains(MorePlatesMod.MODID))
                    .forEach(rs -> event.accept(rs.asItem()));
        }
    }

}
