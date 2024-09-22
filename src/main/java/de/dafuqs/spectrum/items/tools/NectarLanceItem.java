package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import org.jetbrains.annotations.*;

public class NectarLanceItem extends LightGreatswordItem implements SlotBackgroundEffectProvider {

	public NectarLanceItem(ToolMaterial material, int attackDamage, float attackSpeed, float reach, int barColor, Settings settings) {
		super(material, attackDamage, attackSpeed, reach, barColor, settings);
	}

	@Override
	public float getBlockingMultiplier(DamageSource source, ItemStack stack, LivingEntity entity, int usedTime) {
		if (source.isIn(DamageTypeTags.IS_PROJECTILE))
			return 0;

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

	}

	@Override
	public SlotEffect backgroundType(@Nullable PlayerEntity player, ItemStack stack) {
		return SlotEffect.BORDER_FADE;
	}

	@Override
	public int getBackgroundColor(@Nullable PlayerEntity player, ItemStack stack, float tickDelta) {
		return SpectrumStatusEffects.ETERNAL_SLUMBER_COLOR;
	}
}
