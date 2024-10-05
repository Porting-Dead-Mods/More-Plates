package com.portingdeadmods.moreplates.registries;

import com.portingdeadmods.moreplates.MorePlatesMod;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MPItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MorePlatesMod.MODID);

    public static final DeferredItem<Item> INFINITY_PLATE = ITEMS.register("infinity_plate", () -> new Item(new Item.Properties()));

}
