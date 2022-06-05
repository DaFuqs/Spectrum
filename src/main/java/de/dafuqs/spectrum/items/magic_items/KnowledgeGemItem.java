package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.enchanter.EnchanterEnchantable;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.items.SpectrumBannerPatternItem;
import de.dafuqs.spectrum.registries.SpectrumBannerPatterns;
import de.dafuqs.spectrum.registries.SpectrumItems;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPattern;
import io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KnowledgeGemItem extends Item implements ExperienceStorageItem, EnchanterEnchantable, LoomPatternProvider {
	
	private final int maxStorageBase;
	
	// these are copies from the item model file
	// and specify the sprite used for its texture
	protected int[] displayTiers = {1, 10, 25, 50, 100, 250, 500, 1000, 2500, 5000};
	
	public KnowledgeGemItem(Settings settings, int maxStorageBase) {
		super(settings);
		this.maxStorageBase = maxStorageBase;
	}
	
	public static ItemStack getKnowledgeDropStackWithXP(int experience) {
		ItemStack stack = new ItemStack(SpectrumItems.KNOWLEDGE_GEM);
		NbtCompound compound = new NbtCompound();
		compound.putInt("stored_experience", experience);
		compound.putBoolean("do_not_display_store_tooltip", true);
		stack.setNbt(compound);
		return stack;
	}
	
	@Override
	public int getMaxStoredExperience(ItemStack itemStack) {
		int efficiencyLevel = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack);
		return maxStorageBase * (int) Math.pow(10, Math.min(5, efficiencyLevel)); // to not exceed int max
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	public int getTransferableExperiencePerTick(ItemStack itemStack) {
		int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, itemStack);
		return (int) (2 * Math.pow(2, Math.min(10, quickChargeLevel)));
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		super.usageTick(world, user, stack, remainingUseTicks);
		if (user instanceof ServerPlayerEntity serverPlayerEntity) {
			
			int playerExperience = serverPlayerEntity.totalExperience;
			int itemExperience = ExperienceStorageItem.getStoredExperience(stack);
			int transferableExperience = getTransferableExperiencePerTick(stack);
			
			if (serverPlayerEntity.isSneaking()) {
				int maxStorage = getMaxStoredExperience(stack);
				int experienceToTransfer = serverPlayerEntity.isCreative() ? Math.min(transferableExperience, maxStorage - itemExperience) : Math.min(Math.min(transferableExperience, playerExperience), maxStorage - itemExperience);
				
				// Store experience
				if (experienceToTransfer > 0 && itemExperience < maxStorage && removePlayerExperience(serverPlayerEntity, experienceToTransfer)) {
					ExperienceStorageItem.addStoredExperience(stack, experienceToTransfer);
					
					if (remainingUseTicks % 4 == 0) {
						world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
					}
				}
			} else {
				// drain experience
				if (itemExperience > 0 && playerExperience != Integer.MAX_VALUE) {
					int experienceToTransfer = Math.min(Math.min(transferableExperience, itemExperience), Integer.MAX_VALUE - playerExperience);
					
					if (experienceToTransfer > 0) {
						if (!serverPlayerEntity.isCreative()) {
							serverPlayerEntity.addExperience(experienceToTransfer);
						}
						ExperienceStorageItem.removeStoredExperience(stack, experienceToTransfer);
						
						if (remainingUseTicks % 4 == 0) {
							world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		int maxExperience = getMaxStoredExperience(itemStack);
		int storedExperience = ExperienceStorageItem.getStoredExperience(itemStack);
		if (storedExperience == 0) {
			tooltip.add(new LiteralText("0 ").formatted(Formatting.DARK_GRAY).append(new TranslatableText("item.spectrum.knowledge_gem.tooltip.stored_experience", maxExperience).formatted(Formatting.GRAY)));
		} else {
			tooltip.add(new LiteralText(storedExperience + " ").formatted(Formatting.GREEN).append(new TranslatableText("item.spectrum.knowledge_gem.tooltip.stored_experience", maxExperience).formatted(Formatting.GRAY)));
		}
		if (shouldDisplayUsageTooltip(itemStack)) {
			tooltip.add(new TranslatableText("item.spectrum.knowledge_gem.tooltip.use", getTransferableExperiencePerTick(itemStack)).formatted(Formatting.GRAY));
			SpectrumBannerPatternItem.addBannerPatternProviderTooltip(tooltip);
		}
	}
	
	@Override
	public int getEnchantability() {
		return 5;
	}
	
	public boolean shouldDisplayUsageTooltip(ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getNbt();
		return nbtCompound == null || !nbtCompound.getBoolean("do_not_display_store_tooltip");
	}
	
	public boolean removePlayerExperience(@NotNull PlayerEntity playerEntity, int experience) {
		if (playerEntity.isCreative()) {
			return true;
		} else if (playerEntity.totalExperience < experience) {
			return false;
		} else {
			playerEntity.totalExperience -= experience;
			
			// recalculate levels & level progress
			playerEntity.experienceProgress -= (float) experience / (float) playerEntity.getNextLevelExperience();
			while (playerEntity.experienceProgress < 0.0F) {
				float f = playerEntity.experienceProgress * (float) playerEntity.getNextLevelExperience();
				if (playerEntity.experienceLevel > 0) {
					playerEntity.addExperienceLevels(-1);
					playerEntity.experienceProgress = 1.0F + f / (float) playerEntity.getNextLevelExperience();
				} else {
					playerEntity.addExperienceLevels(-1);
					playerEntity.experienceProgress = 0.0F;
				}
			}
			
			return true;
		}
	}
	
	public boolean changedDisplayTier(int currentStoredExperience, int destinationStoredExperience) {
		return getDisplayTierForExperience(currentStoredExperience) != getDisplayTierForExperience(destinationStoredExperience);
	}
	
	public int getDisplayTierForExperience(int experience) {
		for (int i = 0; i < displayTiers.length; i++) {
			if (experience < displayTiers[i]) {
				return i;
			}
		}
		return displayTiers.length;
	}
	
	@Override
	public boolean canAcceptEnchantment(Enchantment enchantment) {
		return enchantment == Enchantments.EFFICIENCY || enchantment == Enchantments.QUICK_CHARGE;
	}
	
	@Override
	public LoomPattern getPattern() {
		return SpectrumBannerPatterns.KNOWLEDGE_GEM;
	}
	
}
