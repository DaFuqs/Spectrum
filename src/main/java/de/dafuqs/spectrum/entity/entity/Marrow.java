package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.sounds.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import javax.annotation.*;

public class Marrow extends AbstractSkeleton {
	
	public Marrow(EntityType<? extends Marrow> entityType, Level level) {
		super(entityType, level);
	}
	
	public static AttributeSupplier.Builder createMarrowAttributes() {
		return AbstractSkeleton.createAttributes()
				.add(Attributes.MAX_HEALTH, 40.0)
				.add(Attributes.MOVEMENT_SPEED, 0.2)
				.add(Attributes.ARMOR_TOUGHNESS, 2.0);
	}
	
	@Override
	protected int getHardAttackInterval() {
		return 15;
	}
	
	@Override
	protected int getAttackInterval() {
		return 30;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SpectrumSoundEvents.ENTITY_MARROW_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SpectrumSoundEvents.ENTITY_MARROW_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SpectrumSoundEvents.ENTITY_MARROW_DEATH;
	}
	
	@Override
	public SoundEvent getStepSound() {
		return SpectrumSoundEvents.ENTITY_MARROW_STEP;
	}
	
	@Override
	protected AbstractArrow getArrow(ItemStack arrowStack, float velocity, @Nullable ItemStack weapon) {
		AbstractArrow abstractArrow = super.getArrow(arrowStack, velocity, weapon);
		if (abstractArrow instanceof Arrow arrow) {
			arrow.addEffect(new MobEffectInstance(SpectrumMobEffects.STIFFNESS, 600));
		}
		return abstractArrow;
	}
	
}
