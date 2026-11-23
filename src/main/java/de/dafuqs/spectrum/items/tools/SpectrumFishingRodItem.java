package de.dafuqs.spectrum.items.tools;

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
import net.neoforged.neoforge.event.*;

import java.util.*;

public abstract class SpectrumFishingRodItem extends FishingRodItem {
	
	public SpectrumFishingRodItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		
		if (player.fishing != null) {
			if (!world.isClientSide()) {
				int i = player.fishing.retrieve(itemStack);
				ItemStack original = itemStack.copy();
				itemStack.hurtAndBreak(i, player, LivingEntity.getSlotForHand(hand));
				if (itemStack.isEmpty()) {
					EventHooks.onPlayerDestroyItem(player, original, hand);
				}
				
			}
			
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
		} else {
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			if (world instanceof ServerLevel serverWorld) {
				var drm = world.registryAccess();
				int luckBonus = EnchantmentHelper.getFishingLuckBonus(serverWorld, itemStack, player);
				int waitTimeReductionTicks = (int) (EnchantmentHelper.getFishingTimeReduction(serverWorld, itemStack, player) * 20.0F);
				int exuberanceLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantmentKeys.EXUBERANCE, itemStack);
				int bigCatchLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantmentKeys.BIG_CATCH, itemStack);
				int serendipityReelLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantmentKeys.SERENDIPITY_REEL, itemStack);
				boolean inventoryInsertion = SpectrumEnchantmentHelper.hasEnchantment(drm, SpectrumEnchantmentKeys.INVENTORY_INSERTION, itemStack);
				boolean shouldSmeltDrops = shouldSmeltDrops(itemStack);
				spawnBobber(player, world, luckBonus, waitTimeReductionTicks, exuberanceLevel, bigCatchLevel, serendipityReelLevel, inventoryInsertion, shouldSmeltDrops);
			}
			
			player.awardStat(Stats.ITEM_USED.get(this));
			player.gameEvent(GameEvent.ITEM_INTERACT_START);
		}
		
		return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
	}
	
	public abstract void spawnBobber(Player user, Level world, int luckOfTheSeaLevel, int lureLevel, int exuberanceLevel, int bigCatchLevel, int serendipityReelLevel, boolean inventoryInsertion, boolean shouldSmeltDrops);
	
	public boolean canFishIn(FluidState fluidState) {
		return fluidState.is(FluidTags.WATER);
	}
	
	public boolean shouldSmeltDrops(ItemStack itemStack) {
		return EnchantmentHelper.hasTag(itemStack, SpectrumEnchantmentTags.SMELTS_MORE_LOOT);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		tooltip.add(Component.translatable("item.spectrum.spectrum_fishing_rods.tooltip").withStyle(ChatFormatting.GRAY));
	}
	
}
