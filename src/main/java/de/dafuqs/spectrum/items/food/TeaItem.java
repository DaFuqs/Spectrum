package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.ApplyFoodEffectsCallback;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class TeaItem extends DrinkItem implements ApplyFoodEffectsCallback {
	
	protected FoodComponent bonusFoodComponentWithScone;
	
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
		if(entity instanceof PlayerEntity player) {
			for(int i = 0; i < player.getInventory().size(); i++) {
				ItemStack sconeStack = player.getInventory().getStack(i);
				if(sconeStack.isOf(SpectrumItems.SCONE)) {
					if (player instanceof ServerPlayerEntity serverPlayerEntity) {
						Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, sconeStack);
						SpectrumAdvancementCriteria.CONSUMED_TEA_WITH_SCONE.trigger(serverPlayerEntity, sconeStack, teaStack);
					}
					
					if (!player.isCreative()) {
						sconeStack.decrement(1);
					}
					player.emitGameEvent(GameEvent.EAT);
					world.playSound(null, player.getX(), player.getY(), player.getZ(), player.getEatSound(sconeStack), SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
					ApplyFoodEffectsCallback.applyFoodComponent(player.world, player, sconeStack.getItem().getFoodComponent());
					
					ApplyFoodEffectsCallback.applyFoodComponent(player.world, player, this.bonusFoodComponentWithScone);
					return;
				}
			}
		}
	}
	
}
