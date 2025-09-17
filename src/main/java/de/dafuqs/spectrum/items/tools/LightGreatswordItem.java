package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

public class LightGreatswordItem extends ParryingSwordItem implements SplitDamageItem {
	
	private final int barColor;
	
	public LightGreatswordItem(Tier material, int attackDamage, float attackSpeed, float crit, float reach, int barColor, Properties settings) {
		super(material, attackDamage, attackSpeed, crit, reach, settings);
		this.barColor = barColor;
	}
	
	@Override
	public float getBlockingMultiplier(DamageSource source, ItemStack stack, LivingEntity entity, int usedTime) {
		if (source.is(DamageTypeTags.IS_PROJECTILE))
			return 0;
		
		if (canPerfectParry(stack, entity, usedTime)) {
			return 0.05F;
		} else if (canBluffParry(stack, entity, usedTime)) {
			return 0.2F;
		} else if (usedTime <= getMaxShieldingTime(entity, stack) / 2F) {
			return 0.5F;
		}
		
		return 0.75F;
	}
	
	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
		super.releaseUsing(stack, world, user, remainingUseTicks);
		
		if (!(user instanceof Player player))
			return;
		
		var maxShieldTime = getMaxShieldingTime(user, stack);
		if (!player.onGround() && maxShieldTime - remainingUseTicks > 5) {
			
			var chargeDir = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
			float chargeStrength = Math.min((float) (maxShieldTime - remainingUseTicks) / maxShieldTime + 0.2F, 1F);
			
			player.push(chargeDir.normalize().scale(getLungeSpeed() * chargeStrength));
			player.playSound(SpectrumSoundEvents.LUNGE, 2F, 0.8F + player.getRandom().nextFloat() * 0.2F);
			MiscPlayerDataAttachmentType.get(player).initiateLungeState();
		}
	}
	
	public float getLungeSpeed() {
		return 1F;
	}
	
	@Override
	public int getBarColor() {
		return barColor;
	}
	
	protected void applyLungeHitEffects(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (target.getType() == EntityType.ENDERMAN)
			return;
		
		var effect = target.isInvertedHealAndHarm() ? MobEffects.REGENERATION : MobEffects.POISON;
		int sharpness = SpectrumEnchantmentHelper.getLevel(target.level().registryAccess(), Enchantments.SHARPNESS, stack);
		target.addEffect(new MobEffectInstance(effect, 20 * (5 + sharpness), 1));
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (attacker instanceof Player player) {
			if (MiscPlayerDataAttachmentType.get(player).isLunging()) {
				MiscPlayerDataAttachmentType.get(player).endLunge();
				target.playSound(SpectrumSoundEvents.LUNGE_CRIT, 1F, 0.9F + target.getRandom().nextFloat() * 0.2F);
				applyLungeHitEffects(stack, target, attacker);
			}
		}
		return super.hurtEnemy(stack, target, attacker);
	}
	
	@Override
	public DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
		var composition = new DamageComposition();
		var source = composition.getPlayerOrEntity(attacker);
		
		if (attacker instanceof Player player && MiscPlayerDataAttachmentType.get(player).isLunging()) {
			source = SpectrumDamageTypes.impaling(player.level(), player);
		}
		
		composition.add(source, damage);
		return composition;
	}
}
