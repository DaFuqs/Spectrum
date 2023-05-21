package de.dafuqs.spectrum.blocks.gravity;

import de.dafuqs.spectrum.items.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

public class FloatItem extends Item implements GravitableItem {
	
	private final float gravityMod;
	
	public FloatItem(Settings settings, float gravityMod) {
		super(settings);
		this.gravityMod = gravityMod;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		applyGravityEffect(stack, world, entity);
	}
	
	@Override
	public float getGravityModInInventory() {
		return (1 - gravityMod) * 2;
	}
	
	@Override
	public double getGravityModForItemEntity() {
		return (1 - gravityMod) * 10;
	}
	
}
