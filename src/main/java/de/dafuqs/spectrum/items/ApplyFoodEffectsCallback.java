package de.dafuqs.spectrum.items;

import com.mojang.datafixers.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

import java.util.*;

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
