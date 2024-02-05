package de.dafuqs.spectrum.blocks.mob_head;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;

import java.util.*;

public class SpectrumSkullBlockItem extends WallStandingBlockItem {
	
	protected final EntityType<?> entityType;
	protected String artistCached;
	
	public SpectrumSkullBlockItem(Block standingBlock, Block wallBlock, Settings settings, EntityType<?> entityType) {
		super(standingBlock, wallBlock, settings);
		this.entityType = entityType;
	}
	
	public static Optional<EntityType<?>> getEntityTypeOfSkullStack(ItemStack itemStack) {
		Item item = itemStack.getItem();
		if (item instanceof SpectrumSkullBlockItem spectrumSkullBlockItem) {
			return Optional.of(spectrumSkullBlockItem.entityType);
		}
		if (Items.CREEPER_HEAD.equals(item)) {
			return Optional.of(EntityType.CREEPER);
		} else if (Items.DRAGON_HEAD.equals(item)) {
			return Optional.of(EntityType.ENDER_DRAGON);
		} else if (Items.ZOMBIE_HEAD.equals(item)) {
			return Optional.of(EntityType.ZOMBIE);
		} else if (Items.SKELETON_SKULL.equals(item)) {
			return Optional.of(EntityType.SKELETON);
		} else if (Items.WITHER_SKELETON_SKULL.equals(item)) {
			return Optional.of(EntityType.WITHER_SKELETON);
		}
		return Optional.empty();
	}
	
}
