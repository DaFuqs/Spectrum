package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.compat.gofish.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.tags.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.material.*;

import java.util.*;

public abstract class SpectrumFishingRodItem extends FishingRodItem {
	
	public SpectrumFishingRodItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		
		PlayerEntityAccessor playerEntityAccessor = ((PlayerEntityAccessor) user);
		if (playerEntityAccessor.getSpectrumBobber() != null) {
			if (!world.isClientSide) {
				int damage = playerEntityAccessor.getSpectrumBobber().use(itemStack);
				itemStack.hurtAndBreak(damage, user, LivingEntity.getSlotForHand(hand));
			}
			
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			user.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
		} else {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			if (world instanceof ServerLevel serverWorld) {
				var drm = world.registryAccess();
				int luckBonus = EnchantmentHelper.getFishingLuckBonus(serverWorld, itemStack, user);
				int waitTimeReductionTicks = (int)(EnchantmentHelper.getFishingTimeReduction(serverWorld, itemStack, user) * 20.0F);
				int exuberanceLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantments.EXUBERANCE, itemStack);
				int bigCatchLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantments.BIG_CATCH, itemStack);
				int serendipityReelLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantments.SERENDIPITY_REEL, itemStack);
				boolean inventoryInsertion = SpectrumEnchantmentHelper.hasEnchantment(drm, SpectrumEnchantments.INVENTORY_INSERTION, itemStack);
				boolean shouldSmeltDrops = shouldSmeltDrops(itemStack);
				spawnBobber(user, world, luckBonus, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, shouldSmeltDrops);
			}
			
			user.awardStat(Stats.ITEM_USED.get(this));
			user.gameEvent(GameEvent.ITEM_INTERACT_START);
		}
		
		return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
	}
	
	public abstract void spawnBobber(Player user, Level world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion, boolean shouldSmeltDrops);
	
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.is(FluidTags.WATER);
	}
	
	public boolean shouldSmeltDrops(ItemStack itemStack) {
		return EnchantmentHelper.hasTag(itemStack, SpectrumEnchantmentTags.SMELTS_MORE_LOOT) || GoFishCompat.hasDeepfry(itemStack);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.spectrum_fishing_rods.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
}
