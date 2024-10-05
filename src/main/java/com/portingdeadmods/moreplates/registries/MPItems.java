package com.portingdeadmods.moreplates.registries;

import com.portingdeadmods.moreplates.MorePlatesMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MPItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MorePlatesMod.MODID);

    public static final DeferredItem<Item> INFINITY_PLATE = ITEMS.register("infinity_plate", () -> new Item(new Item.Properties()));

}
