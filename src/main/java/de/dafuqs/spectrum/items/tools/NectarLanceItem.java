package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.api.status_effect.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import org.jetbrains.annotations.*;

public class NectarLanceItem extends LightGreatswordItem implements SlotBackgroundEffectProvider {

	public NectarLanceItem(ToolMaterial material, int attackDamage, float attackSpeed, float crit, float reach, int barColor, Settings settings) {
		super(material, attackDamage, attackSpeed, crit, reach, barColor, settings);
	}

	@Override
	public float getBlockingMultiplier(DamageSource source, ItemStack stack, LivingEntity entity, int usedTime) {
		if (source.isIn(DamageTypeTags.IS_PROJECTILE)) {
			return 0;
		}
		if (canPerfectParry(stack, entity, usedTime)) {
			return 0.0F;
		}
		else if(canBluffParry(stack, entity, usedTime)) {
			return 0.1F;
		}
		else if (usedTime <= getMaxShieldingTime(entity, stack) / 2F) {
			return 0.25F;
		}
		return 0.6F;
	}

	@Override
	public float getLungeSpeed() {
		return 2F;
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 30;
	}

	@Override
	protected void applyLungeHitEffects(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		var base = (float) attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) + EnchantmentHelper.getAttackDamage(stack, target.getGroup());
		if (target.hasStatusEffect(StatusEffects.POISON)) {
			var effect = target.getStatusEffect(StatusEffects.POISON);
			if (target.removeStatusEffect(StatusEffects.POISON)) {
				assert effect != null;
				applyDoTProc(attacker.getDamageSources().magic(), base + 5F,0.8F, target, effect, false, true);
			}
		}
		else if (target.hasStatusEffect(SpectrumStatusEffects.DEADLY_POISON)) {
			var effect = target.getStatusEffect(SpectrumStatusEffects.DEADLY_POISON);
			if (target.removeStatusEffect(SpectrumStatusEffects.DEADLY_POISON)) {
				assert effect != null;
				applyDoTProc(attacker.getDamageSources().magic(), base + 10F,1.0F, target, effect, true, true);
			}
		}
		else if (target.hasStatusEffect(StatusEffects.WITHER)) {
			var effect = target.getStatusEffect(StatusEffects.WITHER);
			if (target.removeStatusEffect(StatusEffects.WITHER)) {
				assert effect != null;
				applyDoTProc(attacker.getDamageSources().magic(), base + 5F,0.1F, target, effect, true, false);
			}
		}
		else if (SpectrumStatusEffectTags.hasEffectWithTag(target, SpectrumStatusEffectTags.SOPORIFIC)) {
			var scaling = SleepStatusEffect.getSleepScaling(target);
			if (scaling > 0) {
				target.damage(SpectrumDamageTypes.sleep(target.getWorld(),target), scaling);
				target.playSound(SpectrumSoundEvents.DEEP_CRYSTAL_RING, 0.5F, 0.8F + target.getRandom().nextFloat() * 0.4F);
			}
		}
		else {
			var stolenEffect = target.getStatusEffects()
					.stream()
					.filter(instance -> instance.getEffectType().isBeneficial())
					.filter(instance -> !((Incurable) instance).spectrum$isIncurable())
					.findFirst();

			if (stolenEffect.isEmpty() || !target.removeStatusEffect(stolenEffect.get().getEffectType()))
				return;

			var effect = stolenEffect.get();
			var duration = effect.getDuration();
			var amp = effect.getAmplifier();
			var takenDuration = (int) Math.ceil(duration / Math.log10(duration + 1));
			var takenAmp = 0;

			if (attacker.hasStatusEffect(effect.getEffectType()))
				takenAmp += attacker.getStatusEffect(effect.getEffectType()).getAmplifier();

			attacker.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), takenDuration, takenAmp));

			if (amp > 0)
				target.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), duration, amp - 1, effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon()));

			target.playSound(SpectrumSoundEvents.SOFT_HUM, 0.275F, 0.8F + target.getRandom().nextFloat() * 0.4F);
		}
	}

	public static boolean sleepCrits(PlayerEntity player, Entity target) {
		if (!(target instanceof LivingEntity livingEntity))
			return false;

		if (!player.getMainHandStack().isOf(SpectrumItems.NECTAR_LANCE))
			return false;

		if (livingEntity.isSleeping())
			return true;

		var scaling = SleepStatusEffect.getSleepScaling(livingEntity);
		return scaling > 0 && livingEntity.getRandom().nextFloat() <= scaling / 3F;
	}

	private static void applyDoTProc(DamageSource type, float baseDamage, float damageScaling, LivingEntity target, StatusEffectInstance effect, boolean canKill, boolean logScaling) {
		var duration = effect.getDuration() / 20F;
		var level = effect.getAmplifier() + 1;
		var scaling = level * damageScaling;
		var damage = scaling;

		if (logScaling) {
			damage = (float) (Math.log(duration) / Math.log(2) * scaling);

		}

		damage += baseDamage;
		if (!canKill) {
			damage = Math.min(target.getHealth() - 1, damage);
		}

		target.damage(type, damage);
		target.playSound(SpectrumSoundEvents.DEEP_CRYSTAL_RING, 1.25F, 0.9F + target.getRandom().nextFloat() * 0.2F);
	}


	@Override
	public SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		return SlotEffect.BORDER_FADE;
	}

	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		return InkColors.PURPLE_COLOR;
	}
}
