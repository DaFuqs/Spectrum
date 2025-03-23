package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class LightGreatswordItem extends ParryingSwordItem implements SplitDamageItem {

	private final int barColor;

	public LightGreatswordItem(ToolMaterial material, int attackDamage, float attackSpeed, float crit, float reach, int barColor, Settings settings) {
		super(material, attackDamage, attackSpeed, crit, reach, settings);
		this.barColor = barColor;
	}

	@Override
	public float getBlockingMultiplier(DamageSource source, ItemStack stack, LivingEntity entity, int usedTime) {
		if (source.isIn(DamageTypeTags.IS_PROJECTILE))
			return 0;

		if (canPerfectParry(stack, entity, usedTime)) {
			return 0.05F;
		}
		else if(canBluffParry(stack, entity, usedTime)) {
			return 0.2F;
		}
		else if (usedTime <= getMaxShieldingTime(entity, stack) / 2F) {
			return 0.5F;
		}

		return 0.75F;
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		super.onStoppedUsing(stack, world, user, remainingUseTicks);

		if (!(user instanceof PlayerEntity player))
			return;

		var maxShieldTime = getMaxShieldingTime(user, stack);
		if (!player.isOnGround() && maxShieldTime - remainingUseTicks > 5) {
			var  yaw = player.getYaw();
			var pitch = player.getPitch();
			var roll = player.getRoll();

			float f = -MathHelper.sin(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
			float g = -MathHelper.sin((pitch + roll) * (float) (Math.PI / 180.0));
			float h = MathHelper.cos(yaw * (float) (Math.PI / 180.0)) * MathHelper.cos(pitch * (float) (Math.PI / 180.0));
			float chargeStrength = Math.min((float) (maxShieldTime - remainingUseTicks) / maxShieldTime + 0.2F, 1F);

			player.addVelocity(new Vec3d(f, g, h).normalize().multiply(getLungeSpeed() * chargeStrength));
			player.playSound(SpectrumSoundEvents.LUNGE, 2F, 0.8F + player.getRandom().nextFloat() * 0.2F);
			MiscPlayerDataComponent.get(player).initiateLungeState();
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

		var effect = target.isUndead() ? StatusEffects.REGENERATION : StatusEffects.POISON;
		int sharpness = EnchantmentHelper.get(stack).getOrDefault(Enchantments.SHARPNESS, 0);
		target.addStatusEffect(new StatusEffectInstance(effect, 20 * (5 + sharpness), 1));
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (attacker instanceof PlayerEntity player) {
			if (MiscPlayerDataComponent.get(player).isLunging()) {
				MiscPlayerDataComponent.get(player).endLunge();
				target.playSound(SpectrumSoundEvents.LUNGE_CRIT, 1F, 0.9F + target.getRandom().nextFloat() * 0.2F);
				applyLungeHitEffects(stack, target, attacker);
			}
		}
		return super.postHit(stack, target, attacker);
	}

	@Override
	public DamageComposition getDamageComposition(LivingEntity attacker, LivingEntity target, ItemStack stack, float damage) {
		var composition = new DamageComposition();
		var source = composition.getPlayerOrEntity(attacker);

		if (attacker instanceof PlayerEntity player && MiscPlayerDataComponent.get(player).isLunging()) {
			source = SpectrumDamageTypes.impaling(player.getWorld(), player);
		}

		composition.add(source, damage);
		return composition;
	}
}
