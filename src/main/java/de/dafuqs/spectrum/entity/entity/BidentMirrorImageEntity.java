package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import de.dafuqs.spectrum.spells.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.*;

import java.util.Optional;

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
        world.playSound(null, entityHitResult.getEntity().getBlockPos(), SpectrumSoundEvents.MEDIUM_CRYSTAL_RING, SoundCategory.PLAYERS, 1.334F, 0.9F + random.nextFloat() * 0.334F);
        world.playSound(null, entityHitResult.getEntity().getBlockPos(), SpectrumSoundEvents.SHATTER_HEAVY, SoundCategory.PLAYERS, 0.75F, 1.0F  + random.nextFloat() * 0.2F);
        MoonstoneStrike.create(world, this, null, this.getX(), this.getY(), this.getZ(), 1);
        if (!world.isClient) {
            processHit(Optional.ofNullable(entityHitResult.getEntity()), 1F);
        }
        this.discard();
    }
    
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        world.playSound(null, blockHitResult.getBlockPos(), SpectrumSoundEvents.SHATTER_HEAVY, SoundCategory.PLAYERS, 0.75F, 1.0F);
        MoonstoneStrike.create(world, this, null, this.getX(), this.getY(), this.getZ(), 1);
        if (!world.isClient) {
            processHit(Optional.empty(), 0.667F);
        }
        this.discard();
    }

    private void processHit(Optional<Entity> target, float effectMult) {
        var stack = getStack();
        var power = EnchantmentHelper.getLevel(Enchantments.POWER, stack) * 0.3F + 1;
        var efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack);

        var user =  getOwner() instanceof LivingEntity livingOwner ? livingOwner : null;
        LightShardEntity.summonBarrage(world, user, this.getPos(), UniformIntProvider.create(5, 8 + 2 * efficiency),
                () -> new LightShardEntity(world, user, target, effectMult * power, 200 + 40 * efficiency / effectMult)
        );
    }

}
