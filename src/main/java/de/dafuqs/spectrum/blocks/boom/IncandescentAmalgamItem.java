package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.food.beverages.properties.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class IncandescentAmalgamItem extends BlockItem implements DamageAwareItem, FermentedItem {
	
	public IncandescentAmalgamItem(Block block, Settings settings) {
		super(block, settings);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		stack = super.finishUsing(stack, world, user);
		
		user.damage(SpectrumDamageTypes.incandescence(world), 500.0F);
		
		float explosionPower = getExplosionPower(stack, false);
		world.createExplosion(user, SpectrumDamageTypes.incandescence(world), new EntityExplosionBehavior(user), user.getX(), user.getEyeY(), user.getZ(), explosionPower, true, World.ExplosionSourceType.BLOCK);
		
		if (user.isAlive() && user instanceof ServerPlayerEntity serverPlayerEntity && !serverPlayerEntity.isCreative()) {
			Support.grantAdvancementCriterion(serverPlayerEntity, "survive_drinking_incandescent_amalgam", "survived_drinking_incandescent_amalgam");
		}
		
		return stack;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("block.spectrum.incandescent_amalgam.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("block.spectrum.incandescent_amalgam.tooltip_power", getExplosionPower(stack, false)).formatted(Formatting.GRAY));
		if (FermentedItem.isPreviewStack(stack)) {
			tooltip.add(Text.translatable("block.spectrum.incandescent_amalgam.tooltip.preview").formatted(Formatting.GRAY));
		}
	}
	
	@Override
	public void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity) {
		// remove the itemEntity before dealing damage, otherwise it would cause a stack overflow
		ItemStack stack = itemEntity.getStack();
		itemEntity.remove(Entity.RemovalReason.KILLED);
		
		float explosionPower = getExplosionPower(stack, true);
		var world = itemEntity.getWorld();
		world.createExplosion(itemEntity, SpectrumDamageTypes.incandescence(world, itemEntity), new EntityExplosionBehavior(itemEntity), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), explosionPower, true, World.ExplosionSourceType.BLOCK);
	}

	@Override
	public BeverageProperties getBeverageProperties(ItemStack stack) {
		return BeverageProperties.getFromStack(stack);
	}

	public float getExplosionPower(ItemStack stack, boolean useCount) {
		float alcPercent = getBeverageProperties(stack).alcPercent;
		if (alcPercent <= 0) {
			return 6;
		} else {
			return alcPercent * (useCount ? 0.875F + (stack.getCount() / 8F) : 1);
		}
	}
	
}
