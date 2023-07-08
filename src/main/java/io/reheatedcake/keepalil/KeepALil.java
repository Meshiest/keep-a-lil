package io.reheatedcake.keepalil;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SmithingTemplateItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Rarity;
import net.minecraft.util.UseAction;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeepALil implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("keep-a-lil");

	private static HashSet<Item> ITEMS;

	@Override
	public void onInitialize() {

		ITEMS = new HashSet<>();
		ITEMS.add(Items.TORCH);
		ITEMS.add(Items.SOUL_TORCH);
		ITEMS.add(Items.REDSTONE_TORCH);
		ITEMS.add(Items.CLOCK);
		ITEMS.add(Items.FIREWORK_ROCKET);
		ITEMS.add(Items.MAP);
		ITEMS.add(Items.FILLED_MAP);
		ITEMS.add(Items.CARVED_PUMPKIN);
		ITEMS.add(Items.ENDER_PEARL);
		ITEMS.add(Items.ENDER_EYE);
		ITEMS.add(Items.ENDER_CHEST);
		ITEMS.add(Items.LEAD);
		ITEMS.add(Items.SADDLE);
		ITEMS.add(Items.WRITABLE_BOOK);
		ITEMS.add(Items.WRITTEN_BOOK);

		var count = 0;
		for (var item : Registries.ITEM) {
			if (isKeptItem(item.getDefaultStack())) {
				count++;
			}
		}

		LOGGER.info("[keep-a-lil] There are " + count + " different items you can keep on death!");

	}

	public static boolean isKeptItem(ItemStack stack) {
		var item = stack.getItem();

		if (ITEMS.contains(item))
			return true;

		if (stack.getRarity() != Rarity.COMMON)
			return true;
		if (stack.isEnchantable())
			return true;
		if (stack.isDamageable())
			return true;
		if (stack.getUseAction() != UseAction.NONE)
			return true;
		if (stack.isFood())
			return true;
		if (item instanceof SmithingTemplateItem)
			return true;
		if (item instanceof BucketItem)
			return true;

		if (stack.isIn(ItemTags.TOOLS))
			return true;
		if (stack.isIn(ItemTags.COMPASSES))
			return true;
		if (stack.isIn(ItemTags.ARROWS))
			return true;
		if (stack.isIn(ItemTags.BOATS))
			return true;

		if (item instanceof BlockItem) {
			var block = ((BlockItem) item).getBlock();

			if (block instanceof ShulkerBoxBlock)
				return true;
		}

		return false;
	}
}