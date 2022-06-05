package de.dafuqs.spectrum.blocks.gravity;

import de.dafuqs.spectrum.interfaces.GravitableItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
	
	public double getGravityModForItemEntity() {
		return (1 - gravityMod) * 10;
	}
	
}
