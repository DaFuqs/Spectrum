package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

public class Splinterspawn extends Silverfish {
	
	public Splinterspawn(EntityType<? extends Silverfish> entityType, Level level) {
		super(entityType, level);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
		// this.goalSelector.addGoal(5, new SilverfishMergeWithStoneGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
	}
	
	public static AttributeSupplier.Builder createSplinterSpawnAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 24.0)
				.add(Attributes.MOVEMENT_SPEED, 0.3)
				.add(Attributes.ATTACK_DAMAGE, 4.0)
				.add(Attributes.ARMOR, 3.0)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.2);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SpectrumSoundEvents.ENTITY_SPLINTERSPAWN_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SpectrumSoundEvents.ENTITY_SPLINTERSPAWN_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SpectrumSoundEvents.ENTITY_SPLINTERSPAWN_DEATH;
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(SpectrumSoundEvents.ENTITY_SPLINTERSPAWN_STEP, 0.15F, 1.0F);
	}
	
	@Override
	protected int getBaseExperienceReward() {
		return 5;
	}
	
}
