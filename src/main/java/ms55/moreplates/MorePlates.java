package ms55.moreplates;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.registries.Registries;
import ms55.moreplates.client.config.Config;
import ms55.moreplates.common.advancements.BooleanCondition;
import ms55.moreplates.common.data.DataGenerators;
import ms55.moreplates.common.item.ModItems;
import ms55.moreplates.common.plugin.core.PluginLoader;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.*;

@Mod(MorePlates.MODID)
public class MorePlates {
    public static final String MODID = "moreplates";
	public static final String NAME = "More Plates";

    public static final Logger LOGGER = LogManager.getLogger();
    public static final DeferredRegister<CreativeModeTab> TABREGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> MORE_PLATES_TAB = TABREGISTER.register("moreplates", () ->
            CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable("item_group."+MODID+".moreplates_tab"))
                    .icon(()->new ItemStack(ModItems.HAMMER))
                    .displayItems((p,out) -> {
                        out.accept(ModItems.HAMMER);
                    })
                    .build());

	public static final boolean DEBUG = false; //Remember to set this to false before publishing lol

    public MorePlates() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(Config.class);

        FMLJavaModLoadingContext.get().getModEventBus().register(this);

        ModLoadingContext.get().registerConfig(Type.COMMON, Config.COMMON_SPEC, "moreplates.toml");

		try {
			PluginLoader.initializePlugins();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ModItems.init();

        MinecraftForge.EVENT_BUS.register(DataGenerators.class);
    }

    public static boolean isDevEnv() {
        return "fmluserdevclient".equals(System.getenv("target"));
    }

    @SubscribeEvent
    public void registerRecipeSerialziers(RegisterEvent event) {
        CraftingHelper.register(BooleanCondition.Serializer.INSTANCE);
    }
}
