package de.dafuqs.spectrum.items.food;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class DrinkItem extends Item {
	
	public DrinkItem(Settings settings) {
		super(settings);
	}
	
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if (playerEntity instanceof ServerPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
		}
		
		if (!world.isClient) {
			List<StatusEffectInstance> list = PotionUtil.getPotionEffects(stack);
			for (StatusEffectInstance statusEffectInstance : list) {
				if (statusEffectInstance.getEffectType().isInstant()) {
					statusEffectInstance.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0D);
				} else {
					user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
				}
			}
		}
		
		if (playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		
		if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}
			
			if (playerEntity != null) {
				playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		
		user.emitGameEvent(GameEvent.DRINK);
		return super.finishUsing(stack, world, user);
	}
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	public int getMaxUseTime(ItemStack stack) {
		return 40;
	}
	
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}
	
	public SoundEvent getDrinkSound() {
		return SoundEvents.ENTITY_GENERIC_DRINK;
	}
	
	public SoundEvent getEatSound() {
		return SoundEvents.ENTITY_GENERIC_DRINK;
	}
	
}
