package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
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
	
    private final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_ashen_circlet");

	public AshenCircletItem(Settings settings) {
		super(settings);
	}
	
	@Override
    protected Identifier getUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.ashen_circlet.tooltip").formatted(Formatting.GRAY));
	}
	
	public static long getCooldownTicks(@NotNull ItemStack ashenCircletStack, @NotNull World world) {
		NbtCompound nbt = ashenCircletStack.getNbt();
		if(nbt == null || nbt.contains("last_cooldown_start", NbtElement.LONG_TYPE)) {
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
			livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, FIRE_RESISTANCE_EFFECT_DURATION, 1, true, true));
			// TODO: sound
			setCooldown(ashenCircletStack, livingEntity.world);
		}
	}
	
}