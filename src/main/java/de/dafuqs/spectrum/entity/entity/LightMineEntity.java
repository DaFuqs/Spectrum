package de.dafuqs.spectrum.entity.entity;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class LightMineEntity extends LightShardBaseEntity {
    
    private static final int NO_POTION_COLOR = -1;
    private static final TrackedData<Integer> COLOR = DataTracker.registerData(LightMineEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private boolean colorSet;
    
    protected final Set<StatusEffectInstance> effects = Sets.newHashSet();
    
    public LightMineEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public LightMineEntity(World world, LivingEntity owner, Optional<Entity> target, float detectionRange, float damage, float lifeSpanTicks) {
        super(SpectrumEntityTypes.LIGHT_MINE, world, owner, target, detectionRange, damage, lifeSpanTicks);
    }
    
    public static void summonBarrage(World world, LivingEntity user, @Nullable Entity target, List<StatusEffectInstance> effects) {
        summonBarrageInternal(world, user, () -> {
            LightMineEntity entity = new LightMineEntity(world, user, Optional.ofNullable(target), 4, 1.0F, 800);
            entity.setEffects(effects);
            return entity;
        });
    }
    
    public void setEffects(List<StatusEffectInstance> effects) {
        this.effects.addAll(effects);
        if (this.effects.isEmpty()) {
            setColor(16777215);
        } else {
            setColor(PotionUtil.getColor(this.effects));
        }
    }
    
    public int getColor() {
        return this.dataTracker.get(COLOR);
    }
    
    private void setColor(int color) {
        this.colorSet = true;
        this.dataTracker.set(COLOR, color);
    }
    
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        
        if (this.colorSet) {
            nbt.putInt("Color", this.getColor());
        }
        if (!this.effects.isEmpty()) {
            NbtList nbtList = new NbtList();
            for (StatusEffectInstance statusEffectInstance : this.effects) {
                nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
            }
            nbt.put("CustomPotionEffects", nbtList);
        }
    }
    
    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    
        this.setEffects(PotionUtil.getCustomPotionEffects(nbt));
    
        if (nbt.contains("Color", NbtElement.NUMBER_TYPE)) {
            this.setColor(nbt.getInt("Color"));
        } else {
            this.colorSet = false;
            if (this.effects.isEmpty()) {
                this.dataTracker.set(COLOR, NO_POTION_COLOR);
            } else {
                this.dataTracker.set(COLOR, PotionUtil.getColor(this.effects));
            }
        }
    }
    
    @Override
    public Identifier getTexture() {
        return SpectrumCommon.locate("textures/entity/projectile/light_mine.png");
    }
    
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(COLOR, NO_POTION_COLOR);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.world.isClient && this.age % 4 == 0) {
            this.spawnParticles();
        }
    }
    
    private void spawnParticles() {
        if (!this.effects.isEmpty()) {
            int color = this.getColor();
            double d = (double) (color >> 16 & 255) / 255.0;
            double e = (double) (color >> 8 & 255) / 255.0;
            double f = (double) (color & 255) / 255.0;
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), d, e, f);
        }
    }
    
    @Override
    protected void onHitEntity(LivingEntity owner, Entity attacked) {
        super.onHitEntity(owner, attacked);
        
        if (attacked instanceof LivingEntity livingEntity) {
            Entity entity = this.getEffectCause();
            
            Iterator<StatusEffectInstance> var3 = this.effects.iterator();
            StatusEffectInstance statusEffectInstance;
            while (var3.hasNext()) {
                statusEffectInstance = var3.next();
                livingEntity.addStatusEffect(new StatusEffectInstance(statusEffectInstance.getEffectType(), Math.max(statusEffectInstance.getDuration() / 8, 1), statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()), entity);
            }
            if (!this.effects.isEmpty()) {
                var3 = this.effects.iterator();
                while (var3.hasNext()) {
                    statusEffectInstance = var3.next();
                    livingEntity.addStatusEffect(statusEffectInstance, entity);
                }
            }
        }
    }
    
}
