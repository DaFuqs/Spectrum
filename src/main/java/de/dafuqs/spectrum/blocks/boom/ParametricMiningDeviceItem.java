package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.explosion.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ParametricMiningDeviceItem extends ExplosionEffectModifierCarryingBlockItem {
	
	public ParametricMiningDeviceItem(Block block, ExplosionArchetype archetype, Settings settings, String[] tooltips) {
		super(block, archetype, settings, tooltips);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		var stack = user.getStackInHand(hand);
		if (stack.isOf(this)) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SpectrumSoundEvents.BLOCK_PARAMETRIC_MINING_DEVICE_THROWN, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!world.isClient()) {
				ParametricMiningDeviceEntity entity = new ParametricMiningDeviceEntity(world, user);
				entity.setItem(stack);
				entity.setVelocity(user, user.getPitch(), user.getYaw(), 0, 1.5F, 0F);
				world.spawnEntity(entity);
			}
			if (!user.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}
		return TypedActionResult.success(stack);
	}
	
}
