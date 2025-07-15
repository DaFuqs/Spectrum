package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;

import java.util.*;

public class IncandescentAmalgamItem extends BlockItem implements DamageAwareItem, FermentedItem {
	
	public IncandescentAmalgamItem(Block block, Properties settings) {
		super(block, settings);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		stack = super.finishUsingItem(stack, world, user);
		
		user.hurt(SpectrumDamageTypes.incandescence(world), 500.0F);
		
		float explosionPower = getExplosionPower(stack, false);
		world.explode(user, SpectrumDamageTypes.incandescence(world), new EntityBasedExplosionDamageCalculator(user), user.getX(), user.getEyeY(), user.getZ(), explosionPower, true, Level.ExplosionInteraction.BLOCK);
		
		if (user.isAlive() && user instanceof ServerPlayer serverPlayerEntity && !serverPlayerEntity.isCreative()) {
			Support.grantAdvancementCriterion(serverPlayerEntity, "survive_drinking_incandescent_amalgam", "survived_drinking_incandescent_amalgam");
		}
		
		return stack;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("block.spectrum.incandescent_amalgam.tooltip").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("block.spectrum.incandescent_amalgam.tooltip_power", getExplosionPower(stack, false)).withStyle(ChatFormatting.GRAY));
		if (FermentedItem.isPreviewStack(stack))
			tooltip.add(Component.translatable("block.spectrum.incandescent_amalgam.tooltip.preview").withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity) {
		// remove the itemEntity before dealing damage, otherwise it would cause a stack overflow
		ItemStack stack = itemEntity.getItem();
		itemEntity.remove(Entity.RemovalReason.KILLED);
		
		float explosionPower = getExplosionPower(stack, true);
		var world = itemEntity.level();
		world.explode(itemEntity, SpectrumDamageTypes.incandescence(world, itemEntity), new EntityBasedExplosionDamageCalculator(itemEntity), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), explosionPower, true, Level.ExplosionInteraction.BLOCK);
	}
	
	public float getExplosionPower(ItemStack stack, boolean useCount) {
		float alcPercent = stack.getOrDefault(SpectrumDataComponentTypes.BEVERAGE, BeverageComponent.DEFAULT).alcoholPercent();
		return alcPercent <= 0 ? 6 : alcPercent * (useCount ? 0.875F + (stack.getCount() / 8F) : 1);
	}
	
}
