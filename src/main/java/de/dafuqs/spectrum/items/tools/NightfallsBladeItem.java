package de.dafuqs.spectrum.items.tools;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.particle.effect.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.potion.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class NightfallsBladeItem extends SwordItem implements PotionFillable {
	
	private static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("unlocks/equipment/nightfalls_blade");
	
	public NightfallsBladeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public boolean isDamageable() {
		return false;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
	
	@Override
	public int maxEffectCount() {
		return 1;
	}
	
	@Override
	public int maxEffectAmplifier() {
		return 2;
	}
	
	@Override
	public long adjustFinalCostFor(@NotNull InkPoweredStatusEffectInstance instance) {
		return (long) Math.pow(instance.getInkCost().getCost(), 2 + instance.getStatusEffectInstance().getAmplifier());
	}
	
	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if(target.isAlive() && attacker instanceof PlayerEntity player) {
			if (AdvancementHelper.hasAdvancement(player, UNLOCK_IDENTIFIER)) {
				List<InkPoweredStatusEffectInstance> effects = getEffects(stack);
				for(InkPoweredStatusEffectInstance instance : effects) {
					if(InkPowered.tryDrainEnergy(player, instance.getInkCost().getColor(), instance.getInkCost().getCost())) {
						World world = attacker.getWorld();
						if (world.isClient) {
							world.addParticle(new DynamicParticleEffect(ParticleTypes.EFFECT, 0.1F, ColorHelper.colorIntToVec(instance.getStatusEffectInstance().getEffectType().getColor()), 0.5F, 120, true, true),
									target.getParticleX(0.5D), target.getBodyY(0.5D), target.getParticleZ(0.5D),
									world.random.nextFloat() - 0.5, world.random.nextFloat() - 0.5, world.random.nextFloat() - 0.5
							);
						} else {
							target.addStatusEffect(instance.getStatusEffectInstance(), attacker);
						}
					}
				}
			}
		}
		return super.postHit(stack, target, attacker);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return super.hasGlint(stack) || PotionUtil.getCustomPotionEffects(stack).size() > 0;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		appendPotionFillableTooltip(stack, tooltip, Text.translatable("item.spectrum.nightfalls_blade.when_struck"), true);
	}
	
}