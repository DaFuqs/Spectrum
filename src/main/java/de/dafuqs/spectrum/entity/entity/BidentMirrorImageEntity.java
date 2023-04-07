package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.spells.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.sound.*;
import net.minecraft.util.hit.*;
import net.minecraft.world.*;

public class BidentMirrorImageEntity extends BidentEntity {
    
    public BidentMirrorImageEntity(World world) {
        this(SpectrumEntityTypes.BIDENT_MIRROR_IMAGE, world);
    }
    
    public BidentMirrorImageEntity(EntityType<? extends TridentEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.world.isClient) {
            this.world.addParticle(SpectrumParticleTypes.MIRROR_IMAGE, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0, 0, 0);
        }
    }
    
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        MoonstoneStrike.create(world, this, null, this.getX(), this.getY(), this.getZ(), 2);
        this.discard();
    }
    
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        MoonstoneStrike.create(world, this, null, this.getX(), this.getY(), this.getZ(), 2);
        this.discard();
    }
    
    @Override
    protected SoundEvent getHitSound() {
        return SpectrumSoundEvents.BIDENT_HIT_GROUND;
    }
    
}
