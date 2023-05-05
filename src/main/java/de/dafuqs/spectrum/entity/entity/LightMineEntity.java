package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import javax.annotation.*;
import java.util.*;

public class LightMineEntity extends LightShardBaseEntity {
    
    protected @Nullable StatusEffectInstance effect;
    
    public LightMineEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public LightMineEntity(World world, LivingEntity owner, Optional<Entity> target, @Nullable StatusEffectInstance effect, float damageMod, float lifespanMod) {
        super(SpectrumEntityTypes.LIGHT_MINE, world, owner, target, damageMod, lifespanMod);
        
        this.damage = 8;
        this.detectionRange = 4;
        this.effect = effect; // TODO: sync to client, so it can use the effect in the renderer
    }
    
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.effect != null) {
            NbtCompound effectNbt = new NbtCompound();
            this.effect.writeNbt(effectNbt);
            nbt.put("effect", effectNbt);
        }
    }
    
    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("effect", NbtElement.COMPOUND_TYPE)) {
            this.effect = StatusEffectInstance.fromNbt(nbt.getCompound("effect"));
        }
    }
    
    @Override
    public Identifier getTexture() {
        return SpectrumCommon.locate("textures/entity/projectile/light_mine.png");
    }
    
    @Override
    protected void initDataTracker() {
    }
    
    @Override
    protected void onHitEntity(LivingEntity owner, Entity attacked) {
        super.onHitEntity(owner, attacked);
        if (this.effect != null && attacked instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(this.effect);
        }
    }
    
    public @Nullable StatusEffectInstance getEffect() {
        return effect;
    }
    
    public static void summonBarrage(World world, LivingEntity user, @Nullable Entity target, StatusEffectInstance effect) {
        summonBarrageInternal(world, user, () -> new LightMineEntity(world, user, Optional.ofNullable(target), effect, 0.5F, 1.0F));
    }
    
}
