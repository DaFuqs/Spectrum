package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.energy.InkPowered;
import de.dafuqs.spectrum.energy.InkPoweredStatusEffectInstance;
import de.dafuqs.spectrum.items.PotionFillable;
import dev.emi.trinkets.api.SlotReference;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PotionPendantItem extends SpectrumTrinketItem implements PotionFillable {
	
	private final static int TRIGGER_EVERY_X_TICKS = 200;
	private final static int EFFECT_DURATION = TRIGGER_EVERY_X_TICKS + 10;
	
	private final int maxEffectCount;
	private final int maxAmplifier;
	
	public PotionPendantItem(Settings settings, int maxEffectCount, int maxAmplifier, Identifier unlockIdentifier) {
		super(settings, unlockIdentifier);
		this.maxEffectCount = maxEffectCount;
		this.maxAmplifier = maxAmplifier;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		appendPotionFillableTooltip(stack, tooltip, Text.translatable("item.spectrum.potion_pendant.when_worn"), false);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return super.hasGlint(stack) || PotionUtil.getCustomPotionEffects(stack).size() > 0;
	}
	
	@Override
	public int maxEffectCount() {
		return maxEffectCount;
	}
	
	@Override
	public int maxEffectAmplifier() {
		return maxAmplifier;
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		
		if (!entity.getWorld().isClient && entity.getWorld().getTime() % TRIGGER_EVERY_X_TICKS == 0 && entity instanceof PlayerEntity player) {
			for (InkPoweredStatusEffectInstance inkPoweredEffect : InkPoweredStatusEffectInstance.getEffects(stack)) {
				if(InkPowered.tryDrainEnergy(player, inkPoweredEffect.getInkCost())) {
					StatusEffectInstance effect = inkPoweredEffect.getStatusEffectInstance();
					player.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), EFFECT_DURATION, effect.getAmplifier(), effect.isAmbient(), effect.shouldShowParticles(), true));
				}
			}
		}
	}
	
}