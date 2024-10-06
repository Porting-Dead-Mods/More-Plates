package com.portingdeadmods.moreplates.registries;

import com.portingdeadmods.moreplates.MorePlatesMod;
import com.portingdeadmods.moreplates.config.MPConfig;
import com.portingdeadmods.moreplates.content.items.HammerItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class MPItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MorePlatesMod.MODID);

    public static final DeferredItem<Item> INFINITY_PLATE = ITEMS.register("infinity_plate", () -> new Item(new Item.Properties()));

    public static final DeferredItem<HammerItem> HAMMER = ITEMS.register("hammer", () -> new HammerItem(new Item.Properties()));

    public static void registerCustomItems(List<String> customItems) {
        for (String customItem : customItems) {
            String[] parts = customItem.split(":");
            if (parts.length == 2) {
                String namespace = parts[0];
                String itemID = parts[1];
                String plateId = itemID + "_plate";
                DeferredItem<Item> plate = ITEMS.register(plateId,
                        () -> new Item(new Item.Properties()));
                MPConfig.saveIngotPlatePair(namespace + ":" + itemID, MorePlatesMod.MODID + ":" + plateId);
            }
        }
    }
}
