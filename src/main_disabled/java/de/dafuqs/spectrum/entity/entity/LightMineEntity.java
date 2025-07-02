package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.core.particles.*;
import net.minecraft.nbt.*;
import net.minecraft.network.syncher.*;
import net.minecraft.resources.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

public class LightMineEntity extends LightShardBaseEntity {

    private static final int NO_POTION_COLOR = -1;
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(LightMineEntity.class, EntityDataSerializers.INT);
    private boolean colorSet;
	
	protected final Set<MobEffectInstance> effects = Sets.newHashSet();
	
	public LightMineEntity(EntityType<? extends Projectile> entityType, Level world) {
		super(entityType, world);
	}
	
	public LightMineEntity(Level world, LivingEntity owner, float detectionRange, float damage, float lifeSpanTicks) {
		super(SpectrumEntityTypes.LIGHT_MINE, world, owner, detectionRange, damage, lifeSpanTicks);
    }
	
	public static void summonBarrage(Level world, @Nullable LivingEntity user, @Nullable LivingEntity target, Predicate<LivingEntity> targetPredicate, List<MobEffectInstance> effects, Vec3 position, IntProvider count) {
        summonBarrageInternal(world, user, () -> {
			LightMineEntity mine = new LightMineEntity(world, user, 8, 1.0F, 800);
			mine.setEffects(effects);
			return mine;
		}, target, targetPredicate, position, count);
    }
	
	public void setEffects(List<MobEffectInstance> effects) {
        this.effects.addAll(effects);
        if (this.effects.isEmpty()) {
            setColor(16777215);
        } else {
			setColor(PotionContents.getColor(this.effects));
        }
    }

    public int getColor() {
		return this.entityData.get(COLOR);
    }
    
    private void setColor(int color) {
        this.colorSet = true;
		this.entityData.set(COLOR, color);
    }
    
    @Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
        
        if (this.colorSet) {
            nbt.putInt("Color", this.getColor());
        }
        if (!this.effects.isEmpty()) {
			CodecHelper.writeNbt(nbt, "custom_potion_effects", MobEffectInstance.CODEC.listOf(), this.effects.stream().toList());
        }
    }
    
    @Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		
		this.setEffects(CodecHelper.fromNbt(MobEffectInstance.CODEC.listOf(), nbt.get("custom_potion_effects"), List.of()));
		
		if (nbt.contains("Color", Tag.TAG_ANY_NUMERIC)) {
            this.setColor(nbt.getInt("Color"));
        } else {
            this.colorSet = false;
            if (this.effects.isEmpty()) {
				this.entityData.set(COLOR, NO_POTION_COLOR);
            } else {
				this.entityData.set(COLOR, PotionContents.getColor(this.effects));
            }
        }
    }
    
    @Override
	public ResourceLocation getTextureLocation() {
        return SpectrumCommon.locate("textures/entity/projectile/light_mine.png");
    }
    
    @Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(COLOR, NO_POTION_COLOR);
    }
    
    @Override
    public void tick() {
        super.tick();
		if (this.level().isClientSide() && this.tickCount % 4 == 0) {
            this.spawnParticles();
        }
    }
    
    private void spawnParticles() {
        if (!this.effects.isEmpty()) {
            int color = this.getColor();
			this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, color), this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
        }
    }
    
    @Override
    protected void onHitEntity(LivingEntity attacked) {
        super.onHitEntity(attacked);
		
		Entity attacker = this.getEffectSource();
		
		Iterator<MobEffectInstance> var3 = this.effects.iterator();
		MobEffectInstance statusEffectInstance;
        while (var3.hasNext()) {
            statusEffectInstance = var3.next();
			attacked.addEffect(new MobEffectInstance(statusEffectInstance.getEffect(), Math.max(statusEffectInstance.getDuration() / 8, 1), statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.isVisible()), attacker);
        }
        if (!this.effects.isEmpty()) {
            var3 = this.effects.iterator();
            while (var3.hasNext()) {
                statusEffectInstance = var3.next();
				attacked.addEffect(statusEffectInstance, attacker);
            }
        }
    }
    
}
