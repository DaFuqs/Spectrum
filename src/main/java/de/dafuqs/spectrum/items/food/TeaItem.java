package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

import java.util.*;

public class TeaItem extends DrinkItem implements ApplyFoodEffectsCallback {
	
	protected final FoodComponent bonusFoodComponentWithScone;
	
	public TeaItem(Settings settings, FoodComponent bonusFoodComponentWithScone) {
		super(settings);
		this.bonusFoodComponentWithScone = bonusFoodComponentWithScone;
	}
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		NbtCompound nbtCompound = itemStack.getNbt();
		if (nbtCompound != null && nbtCompound.contains("Milk")) {
			tooltip.add(Text.translatable("item.spectrum.restoration_tea.tooltip_milk"));
		}
	}
	
	@Override
	public void afterConsumption(World world, ItemStack teaStack, LivingEntity entity) {
		if (entity instanceof PlayerEntity player) {
			for (int i = 0; i < player.getInventory().size(); i++) {
				ItemStack sconeStack = player.getInventory().getStack(i);
				if (sconeStack.isOf(SpectrumItems.SCONE)) {
					if (player instanceof ServerPlayerEntity serverPlayerEntity) {
						Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, sconeStack);
						SpectrumAdvancementCriteria.CONSUMED_TEA_WITH_SCONE.trigger(serverPlayerEntity, sconeStack, teaStack);
					}
					
					world.playSound(null, player.getX(), player.getY(), player.getZ(), player.getEatSound(sconeStack), SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
					ApplyFoodEffectsCallback.applyFoodComponent(player.world, player, sconeStack.getItem().getFoodComponent());
					
					ApplyFoodEffectsCallback.applyFoodComponent(player.world, player, this.bonusFoodComponentWithScone);
					
					if (!player.isCreative()) {
						sconeStack.decrement(1);
					}
					player.emitGameEvent(GameEvent.EAT);
					
					return;
				}
			}
		}
	}
	
}
