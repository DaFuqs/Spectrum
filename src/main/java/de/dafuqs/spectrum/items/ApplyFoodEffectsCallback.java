package de.dafuqs.spectrum.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public interface ApplyFoodEffectsCallback {
	
	/**
	 * Called when an item is consumed by an entity
	 *
	 * @param stack  the stack that is consumed
	 * @param entity the entity consuming the item
	 */
	void afterConsumption(World world, ItemStack stack, LivingEntity entity);
	
	static void applyFoodComponent(World world, LivingEntity entity, FoodComponent foodComponent) {
		if (entity instanceof PlayerEntity player) {
			player.getHungerManager().add(foodComponent.getHunger(), foodComponent.getSaturationModifier());
		}
		
		List<Pair<StatusEffectInstance, Float>> list = foodComponent.getStatusEffects();
		for (Pair<StatusEffectInstance, Float> statusEffectInstanceFloatPair : list) {
			if (!world.isClient && statusEffectInstanceFloatPair.getFirst() != null && world.random.nextFloat() < statusEffectInstanceFloatPair.getSecond()) {
				entity.addStatusEffect(new StatusEffectInstance(statusEffectInstanceFloatPair.getFirst()));
			}
		}
	}
	
}
