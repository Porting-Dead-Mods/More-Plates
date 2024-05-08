package ms55.moreplates.common.data;

import ms55.moreplates.MorePlates
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = MorePlates.MODID, bus = Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

    		generator.addProvider(event.includeClient(), new ItemModels(generator.getPackOutput(), MorePlates.,helper));
    		generator.addProvider(event.includeClient(), new Language(generator));

    	if (event.includeServer()) {
    		generator.addProvider(event.includeServer(), new Recipes(generator));

    		BlockTags blockTags = new BlockTags(generator, helper);
    		generator.addProvider(event.includeServer(), blockTags);
    		generator.addProvider(event.includeServer(), new ItemTags(generator, blockTags, helper));
        }
    }
}