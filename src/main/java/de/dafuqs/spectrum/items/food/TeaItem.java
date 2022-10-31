package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.items.ApplyFoodEffectsCallback;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeaItem extends Item implements ApplyFoodEffectsCallback {
	
	protected FoodComponent bonusFoodComponentWithScone;
	
	public TeaItem(Settings settings, FoodComponent bonusFoodComponentWithScone) {
		super(settings);
		this.bonusFoodComponentWithScone = bonusFoodComponentWithScone;
	}
	
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		super.finishUsing(stack, world, user);
		
		if (user instanceof ServerPlayerEntity serverPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
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
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		NbtCompound nbtCompound = itemStack.getNbt();
		if (nbtCompound != null && nbtCompound.contains("Milk")) {
			tooltip.add(new TranslatableText("item.spectrum.restoration_tea.tooltip_milk"));
		}
	}
	
	@Nullable
	@Override
	public FoodComponent getFoodComponent() {
		return super.getFoodComponent();
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
					world.emitGameEvent(player, GameEvent.EAT, player.getCameraBlockPos());
					world.playSound(null, player.getX(), player.getY(), player.getZ(), player.getEatSound(sconeStack), SoundCategory.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
					ApplyFoodEffectsCallback.applyFoodComponent(player.world, player, sconeStack.getItem().getFoodComponent());
					
					ApplyFoodEffectsCallback.applyFoodComponent(player.world, player, this.bonusFoodComponentWithScone);
					return;
				}
			}
		}
	}
	
}
