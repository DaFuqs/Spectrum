package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.sounds.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class Koi extends AbstractSchoolingFish {
	
    public Koi(EntityType<? extends Koi> entityType, Level level) {
        super(entityType, level);
    }
	
	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
		this.getAttribute(Attributes.SCALE).setBaseValue(0.75 + level.getRandom().nextDouble() * 0.75);
		return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
	}
	
	@Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(SpectrumItems.BUCKET_OF_KOI.get());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COD_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.COD_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }
	
	public static AttributeSupplier.Builder createKoiAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 6.0)
				.add(Attributes.MOVEMENT_SPEED, 0.4);
	}
	
}
