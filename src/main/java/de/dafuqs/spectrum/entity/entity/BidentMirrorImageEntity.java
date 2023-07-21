package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.spells.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.*;

public class BidentMirrorImageEntity extends BidentBaseEntity {
    
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
        MoonstoneStrike.create(world, this, null, this.getX(), this.getY(), this.getZ(), 1);
        if (!world.isClient) {
            LightShardEntity.summonBarrage(world, getOwner() instanceof LivingEntity livingOwner ? livingOwner : null, entityHitResult.getEntity(), this.getPos(), UniformIntProvider.create(1, 3));
        }
        this.discard();
    }
    
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        MoonstoneStrike.create(world, this, null, this.getX(), this.getY(), this.getZ(), 1);
        if (!world.isClient) {
            LightShardEntity.summonBarrage(world, getOwner() instanceof LivingEntity livingOwner ? livingOwner : null, null, this.getPos(), UniformIntProvider.create(1, 3));
        }
        this.discard();
    }
    
}
