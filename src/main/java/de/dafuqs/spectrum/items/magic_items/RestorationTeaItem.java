package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;

public class RestorationTeaItem extends Item {
	
	private static final int MAX_USE_TIME = 40;
	private static final int EFFECT_DURATION_TICKS = 1200;
	
	public RestorationTeaItem(Settings settings) {
		super(settings);
	}
	
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		super.finishUsing(stack, world, user);
		
		if (user instanceof ServerPlayerEntity serverPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		
		if (!world.isClient) {
			user.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.IMMUNITY, EFFECT_DURATION_TICKS, 0, true, true));
		}
		
		if (stack.isEmpty()) {
			return new ItemStack(Items.GLASS_BOTTLE);
		} else {
			if (user instanceof PlayerEntity playerEntity && !((PlayerEntity) user).getAbilities().creativeMode) {
				Support.givePlayer(playerEntity, new ItemStack(Items.GLASS_BOTTLE));
			}
			
			return stack;
		}
	}
	
	public int getMaxUseTime(ItemStack stack) {
		return MAX_USE_TIME;
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
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		tooltip.add(new TranslatableText("item.spectrum.restoration_tea.tooltip"));
		tooltip.add(new TranslatableText("item.spectrum.restoration_tea.tooltip2"));
		
		NbtCompound nbtCompound = itemStack.getNbt();
		if (nbtCompound != null && nbtCompound.contains("Milk")) {
			tooltip.add(new TranslatableText("item.spectrum.restoration_tea.tooltip_milk"));
		}
	}
	
}
