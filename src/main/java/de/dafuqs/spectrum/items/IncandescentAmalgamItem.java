package de.dafuqs.spectrum.items;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class IncandescentAmalgamItem extends BlockItem implements DamageAwareItem {
	
	public IncandescentAmalgamItem(Block block, Settings settings) {
		super(block, settings);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		stack = super.finishUsing(stack, world, user);
		
		user.damage(SpectrumDamageSources.incandescence(world), 500.0F);
		world.createExplosion(user, SpectrumDamageSources.incandescence(world), new EntityExplosionBehavior(user), user.getX(), user.getY(), user.getZ(), 2.0F, false, World.ExplosionSourceType.BLOCK);
		world.createExplosion(user, SpectrumDamageSources.incandescence(world), new EntityExplosionBehavior(user), user.getX(), user.getY(), user.getZ(), 10.0F, true, World.ExplosionSourceType.NONE);
		
		if (user.isAlive() && user instanceof ServerPlayerEntity serverPlayerEntity && !serverPlayerEntity.isCreative()) {
			Support.grantAdvancementCriterion(serverPlayerEntity, "survive_drinking_incandescent_amalgam", "survived_drinking_incandescent_amalgam");
		}
		
		return stack;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("block.spectrum.incandescent_amalgam.tooltip"));
	}
	
	@Override
	public void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity) {
		// remove the itemEntity before dealing damage, otherwise it would cause a stack overflow
		itemEntity.remove(Entity.RemovalReason.KILLED);
		
		int stackCount = itemEntity.getStack().getCount();
		World itemWorld = itemEntity.world;
		itemWorld.createExplosion(itemEntity, SpectrumDamageSources.incandescence(itemWorld), new EntityExplosionBehavior(itemEntity), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 1.0F + stackCount / 16F, false, World.ExplosionSourceType.BLOCK);
		itemWorld.createExplosion(itemEntity, SpectrumDamageSources.incandescence(itemWorld), new EntityExplosionBehavior(itemEntity), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 8.0F + stackCount / 8F, true, World.ExplosionSourceType.NONE);
	}
	
}
