package com.portingdeadmods.moreplates.content.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HammerItem extends Item {
    public HammerItem(Properties properties) {
        super(properties.durability(128));
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.isDamaged();
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack hammer = itemStack.copy();
        int newDamage = hammer.getDamageValue() + 1;
        if (newDamage >= hammer.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        hammer.setDamageValue(newDamage);
        return hammer;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}
