package de.dafuqs.spectrum.blocks.gravity;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class FloatItem extends Item implements GravitableItem {
	
	private final float gravityMod;
	
	public FloatItem(Properties settings, float gravityMod) {
		super(settings);
		this.gravityMod = gravityMod;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		applyGravity(stack, world, entity);
	}
	
	@Override
	public float getGravityMod(ItemStack stack) {
		return this.gravityMod;
	}
	
}
