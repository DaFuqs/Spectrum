package de.dafuqs.spectrum.items.food;

import com.google.common.collect.Lists;
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
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public abstract class BeverageItem extends Item {
	
	// wrapper for beverage itemstack nbt
	// individual for each beverage
	public abstract static class BeverageProperties {
		
		public long ageDays = 0;
		public float alcPercent = 0;
		public float thickness = 0;
		public List<StatusEffectInstance> statusEffects;
		
		public BeverageProperties(long ageDays, float alcPercent, float thickness, List<StatusEffectInstance> statusEffects) {
			this.ageDays = ageDays;
			this.alcPercent = alcPercent;
			this.thickness = thickness;
			this.statusEffects = statusEffects;
		}
		
		public BeverageProperties(NbtCompound nbtCompound) {
			this.statusEffects = Lists.newArrayList();
			if (nbtCompound != null) {
				this.ageDays = nbtCompound.contains("AgeDays") ? nbtCompound.getLong("AgeDays") : 0;
				this.alcPercent = nbtCompound.contains("AlcPercent") ? nbtCompound.getFloat("AlcPercent") : 0;
				this.thickness = nbtCompound.contains("Thickness") ? nbtCompound.getFloat("Thickness") : 0;
				PotionUtil.getCustomPotionEffects(nbtCompound, statusEffects);
			}
		}
		
		public void addTooltip(ItemStack itemStack, List<Text> tooltip) {
			tooltip.add(new TranslatableText("item.spectrum.beverage.tooltip.alc_percent", alcPercent).formatted(Formatting.GRAY));
			tooltip.add(new TranslatableText("item.spectrum.beverage.tooltip.age_days", ageDays).formatted(Formatting.GRAY));
			PotionUtil.buildTooltip(itemStack, tooltip, 1.0F);
		}
		
		protected NbtCompound toNbt(NbtCompound nbtCompound) {
			nbtCompound.putLong("AgeDays", this.ageDays);
			nbtCompound.putFloat("AlcPercent", this.alcPercent);
			nbtCompound.putFloat("Thickness", this.thickness);
			
			NbtList nbtList = nbtCompound.getList("CustomPotionEffects", 9);
			for (StatusEffectInstance statusEffectInstance : this.statusEffects) {
				nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
			}
			nbtCompound.put("CustomPotionEffects", nbtList);
			
			return nbtCompound;
		}
		
		protected ItemStack getStack(ItemStack itemStack) {
			NbtCompound compound = itemStack.getOrCreateNbt();
			toNbt(compound);
			itemStack.setNbt(compound);
			return itemStack;
		}
		
	}
	
	public BeverageItem(Settings settings) {
		super(settings);
	}
	
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
		if (playerEntity instanceof ServerPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
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
			if (!playerEntity.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}
		
		if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}
			if (playerEntity != null) {
				playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
			}
		}
		
		world.emitGameEvent(user, GameEvent.DRINKING_FINISH, user.getCameraBlockPos());
		return stack;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	public abstract BeverageProperties getBeverageProperties(ItemStack itemStack);
	
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		getBeverageProperties(itemStack).addTooltip(itemStack, tooltip);
	}
	
}
