package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AshenCircletItem extends SpectrumTrinketItem {
	
	public static final int FIRE_RESISTANCE_EFFECT_DURATION = 600;
	public static final long COOLDOWN_TICKS = 3000;
	
	public static final float LAVA_MOVEMENT_SPEED_MOD = 0.9F; // vanilla uses 0.5 to slow the player down to half its speed
	public static final float LAVA_VIEW_DISTANCE_MIN = -4.0F;
	public static final float LAVA_VIEW_DISTANCE_MAX = 8.0F;
	public static final float LAVA_VIEW_DISTANCE_MIN_POTION = -8.0F;
	public static final float LAVA_VIEW_DISTANCE_MAX_POTION = 16.0F;
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		if(entity.isOnFire()) {
			entity.setFireTicks(0);
		}
	}
	
	public AshenCircletItem(Settings settings) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_ashen_circlet"));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.ashen_circlet.tooltip").formatted(Formatting.GRAY));
		tooltip.add(new TranslatableText("item.spectrum.ashen_circlet.tooltip2").formatted(Formatting.GRAY));
		
		if(world != null) {
			long cooldownTicks = getCooldownTicks(stack, world);
			if(cooldownTicks == 0) {
				tooltip.add(new TranslatableText("item.spectrum.ashen_circlet.tooltip.cooldown_full"));
			} else {
				tooltip.add(new TranslatableText("item.spectrum.ashen_circlet.tooltip.cooldown_seconds", cooldownTicks / 20));
			}
		}
	}
	
	public static long getCooldownTicks(@NotNull ItemStack ashenCircletStack, @NotNull World world) {
		NbtCompound nbt = ashenCircletStack.getNbt();
		if(nbt == null || !nbt.contains("last_cooldown_start", NbtElement.LONG_TYPE)) {
			return 0;
		} else {
			long lastCooldownStart = nbt.getLong("last_cooldown_start");
			return Math.max(0, lastCooldownStart - world.getTime() + COOLDOWN_TICKS);
		}
	}
	
	private static void setCooldown(@NotNull ItemStack ashenCircletStack, @NotNull World world) {
		NbtCompound nbt = ashenCircletStack.getOrCreateNbt();
		nbt.putLong("last_cooldown_start", world.getTime());
		ashenCircletStack.setNbt(nbt);
	}
	
	public static void grantFireResistance(@NotNull ItemStack ashenCircletStack, @NotNull LivingEntity livingEntity) {
		if(!livingEntity.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
			livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, FIRE_RESISTANCE_EFFECT_DURATION, 0, true, true));
			livingEntity.world.playSound(null, livingEntity.getBlockPos(), SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
			setCooldown(ashenCircletStack, livingEntity.world);
		}
	}
	
}