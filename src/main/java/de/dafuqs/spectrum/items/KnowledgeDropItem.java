package de.dafuqs.spectrum.items;

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

public class KnowledgeDropItem extends Item implements ExperienceStorageItem {
	
	public int maxStorage;
	
	public KnowledgeDropItem(Settings settings, int maxStorage) {
		super(settings);
		this.maxStorage = maxStorage;
	}
	
	@Override
	public int getStoredExperience(ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getNbt();
		if(nbtCompound == null || !nbtCompound.contains("stored_experience", NbtElement.INT_TYPE)) {
			return 0;
		} else {
			return nbtCompound.getInt("stored_experience");
		}
	}
	
	@Override
	public int addStoredExperience(ItemStack itemStack, int amount) {
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();
		if(!nbtCompound.contains("stored_experience", NbtElement.INT_TYPE)) {
			nbtCompound.putInt("stored_experience", amount);
			itemStack.setNbt(nbtCompound);
			return 0;
		} else {
			int existingStoredExperience = nbtCompound.getInt("stored_experience");
			int experienceOverflow = maxStorage - amount - existingStoredExperience;
			
			if(experienceOverflow < 0) {
				nbtCompound.putInt("stored_experience", maxStorage);
				itemStack.setNbt(nbtCompound);
				return -experienceOverflow;
			} else {
				nbtCompound.putInt("stored_experience", existingStoredExperience + amount);
				itemStack.setNbt(nbtCompound);
				return 0;
			}
		}
	}
	
	@Override
	public boolean removeStoredExperience(ItemStack itemStack, int amount) {
		NbtCompound nbtCompound = itemStack.getNbt();
		if(nbtCompound == null || !nbtCompound.contains("stored_experience", NbtElement.INT_TYPE)) {
			return false;
		} else {
			int existingStoredExperience = nbtCompound.getInt("stored_experience");
			if(existingStoredExperience < amount) {
				return false;
			} else {
				nbtCompound.putInt("stored_experience", existingStoredExperience - amount);
				itemStack.setNbt(nbtCompound);
				return true;
			}
		}
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
			int itemExperience = getStoredExperience(stack);
			
			if (serverPlayerEntity.isSneaking()) {
				// Store experience
				if(itemExperience < maxStorage && removePlayerExperience(serverPlayerEntity, 1)) {
					addStoredExperience(stack, 1);
					
					if(remainingUseTicks % 4 ==0) {
						world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
					}
				}
			} else {
				// drain experience
				if(itemExperience > 0 && playerExperience != Integer.MAX_VALUE) {
					serverPlayerEntity.addExperience(1);
					removeStoredExperience(stack, 1);
					
					if(remainingUseTicks % 4 ==0) {
						world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
					}
				}
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		int storedExperience = getStoredExperience(itemStack);
		if(storedExperience == 0) {
			tooltip.add(new LiteralText("0 ").formatted(Formatting.DARK_GRAY).append(new TranslatableText("item.spectrum.knowledge_drop.tooltip.stored_experience", storedExperience).formatted(Formatting.GRAY)));
		} else {
			tooltip.add(new LiteralText(storedExperience + " ").formatted(Formatting.GREEN).append(new TranslatableText("item.spectrum.knowledge_drop.tooltip.stored_experience", storedExperience).formatted(Formatting.GRAY)));
		}
		tooltip.add(new TranslatableText("item.spectrum.knowledge_drop.tooltip.use", storedExperience).formatted(Formatting.GRAY));
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
