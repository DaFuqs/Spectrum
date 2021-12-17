package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.items.ExperienceStorageItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
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

public class KnowledgeGemItem extends Item implements ExperienceStorageItem {
	
	public int maxStorage;
	
	public KnowledgeGemItem(Settings settings, int maxStorage) {
		super(settings);
		this.maxStorage = maxStorage;
	}
	
	@Override
	public int getMaxStoredExperience() {
		return maxStorage;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
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
		if(user instanceof ServerPlayerEntity serverPlayerEntity) {
			
			int playerExperience = serverPlayerEntity.totalExperience;
			int itemExperience = ExperienceStorageItem.getStoredExperience(stack);
			
			if (serverPlayerEntity.isSneaking()) {
				// Store experience
				if(itemExperience < maxStorage && removePlayerExperience(serverPlayerEntity, 1)) {
					ExperienceStorageItem.addStoredExperience(stack, 1);
					
					if(remainingUseTicks % 4 == 0) {
						world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
					}
				}
			} else {
				// drain experience
				if(itemExperience > 0 && playerExperience != Integer.MAX_VALUE) {
					serverPlayerEntity.addExperience(1);
					ExperienceStorageItem.removeStoredExperience(stack, 1);
					
					if(remainingUseTicks % 4 == 0) {
						world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
					}
				}
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		int storedExperience = ExperienceStorageItem.getStoredExperience(itemStack);
		if(storedExperience == 0) {
			tooltip.add(new LiteralText("0 ").formatted(Formatting.DARK_GRAY).append(new TranslatableText("item.spectrum.knowledge_gem.tooltip.stored_experience", storedExperience).formatted(Formatting.GRAY)));
		} else {
			tooltip.add(new LiteralText(storedExperience + " ").formatted(Formatting.GREEN).append(new TranslatableText("item.spectrum.knowledge_gem.tooltip.stored_experience", storedExperience).formatted(Formatting.GRAY)));
		}
		if(shouldDisplayUsageTooltip(itemStack)) {
			tooltip.add(new TranslatableText("item.spectrum.knowledge_gem.tooltip.use", storedExperience).formatted(Formatting.GRAY));
		}
	}
	
	public boolean shouldDisplayUsageTooltip(ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getNbt();
		return nbtCompound == null || !nbtCompound.getBoolean("do_not_display_store_tooltip");
	}
	
	public boolean removePlayerExperience(@NotNull PlayerEntity playerEntity, int experience) {
		if(playerEntity.isCreative()) {
			return true;
		} else if(playerEntity.totalExperience < experience) {
			return false;
		} else {
			playerEntity.totalExperience -= experience;
			
			// recalculate levels & level progress
			playerEntity.experienceProgress -= (float)experience / (float)playerEntity.getNextLevelExperience();
			while(playerEntity.experienceProgress < 0.0F) {
				float f = playerEntity.experienceProgress * (float)playerEntity.getNextLevelExperience();
				if (playerEntity.experienceLevel > 0) {
					playerEntity.addExperienceLevels(-1);
					playerEntity.experienceProgress = 1.0F + f / (float)playerEntity.getNextLevelExperience();
				} else {
					playerEntity.addExperienceLevels(-1);
					playerEntity.experienceProgress = 0.0F;
				}
			}
			
			return true;
		}
	}
	
}
