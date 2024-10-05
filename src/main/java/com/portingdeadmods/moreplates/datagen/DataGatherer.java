package com.portingdeadmods.moreplates.datagen;

import com.portingdeadmods.moreplates.MorePlatesMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = MorePlatesMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGatherer {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        BlockTagProvider blockTagProvider = new BlockTagProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), new RecipesProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ItemTagProvider(output, lookupProvider, blockTagProvider.contentsGetter()));
    }
}
