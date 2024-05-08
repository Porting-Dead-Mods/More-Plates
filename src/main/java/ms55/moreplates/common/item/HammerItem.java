package ms55.moreplates.common.item;

import java.util.Random;

import javax.annotation.Nonnull;

import ms55.moreplates.client.config.Config;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HammerItem extends Item {

    private static final Random RAND = new Random();

	public HammerItem() {
		super(new Item.Properties()
                .stacksTo(1)
                .defaultDurability(Config.GENERAL.DURABILITY_HAMMER.get())
                .setNoRepair());
	}

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 150;
    }

	/*@Override
    public int getDamage(ItemStack stack) {
        return !stack.hasTag() ? getMaxDamage(stack) : stack.getOrCreateTag().getInt("Damage");
    }*/

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
    	ItemStack container = stack.copy();
		if (container.attemptDamageItem(1, RAND, null)) {
			return ItemStack.EMPTY;
		} else {
			return container;
		}
    }

    @Override
	public boolean isEnchantable(@Nonnull ItemStack stack) {
		return true;
	}

    @Override
    public int getEnchantmentValue() {
        return 14;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.MENDING;
    }
}