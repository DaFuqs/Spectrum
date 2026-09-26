package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.fluids.*;
import org.jspecify.annotations.*;

public class Koi extends AbstractFish {
	
	protected final int SCALE_DROP_TIME_BASE = 24000;
	protected int scaleDropTime;
	
    public Koi(EntityType<? extends Koi> entityType, Level level) {
        super(entityType, level);
		this.scaleDropTime = SCALE_DROP_TIME_BASE + this.getRandom().nextInt(SCALE_DROP_TIME_BASE);
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
        return SpectrumSoundEvents.ENTITY_KOI_AMBIENT;
    }
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return SpectrumSoundEvents.ENTITY_KOI_HURT;
	}

    @Override
    protected SoundEvent getDeathSound() {
        return SpectrumSoundEvents.ENTITY_KOI_DEATH;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SpectrumSoundEvents.ENTITY_KOI_FLOP;
    }
	
	public static AttributeSupplier.Builder createKoiAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 6.0)
				.add(Attributes.MOVEMENT_SPEED, 0.8);
	}
	
	@Override
	public void aiStep() {
		super.aiStep();
		
		if (this.isAlive() && isInFluidType(SpectrumFluids.LIQUID_CRYSTAL_TYPE.get())) {
			if(this.level().isClientSide()) {
				this.level()
						.addParticle(
								ParticleTypes.PORTAL,
				this.getRandomX(0.5),
						this.getRandomY() - 0.25,
						this.getRandomZ(0.5),
						(this.random.nextDouble() - 0.5) * 2.0,
						-this.random.nextDouble(),
						(this.random.nextDouble() - 0.5) * 2.0);
			} else if(--this.scaleDropTime <= 0) {
				this.spawnAtLocation(SpectrumItems.SILVER_SCALE);
				this.scaleDropTime = SCALE_DROP_TIME_BASE + this.getRandom().nextInt(SCALE_DROP_TIME_BASE);
			}
		}
	}
	
	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putInt("scale_drop_time", this.scaleDropTime);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		if (nbt.contains("scale_drop_time")) {
			this.scaleDropTime = nbt.getInt("scale_drop_time");
		}
	}
	
}
