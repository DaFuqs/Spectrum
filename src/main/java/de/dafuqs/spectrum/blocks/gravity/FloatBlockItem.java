package de.dafuqs.spectrum.blocks.gravity;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

public class FloatBlockItem extends BlockItem implements GravitableItem {
	
	protected final float gravityMod;
	
	public FloatBlockItem(Block block, Settings settings, float gravityMod) {
		super(block, settings);
		this.gravityMod = gravityMod;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		applyGravity(stack, world, entity);
	}
	
	@Override
	public float getGravityMod() {
		return gravityMod;
	}
	
}
