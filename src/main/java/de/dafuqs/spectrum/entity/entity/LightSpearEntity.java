package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import javax.annotation.*;
import java.util.*;

public class LightSpearEntity extends LightShardBaseEntity {
    
    public LightSpearEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
        
        damage = 8F;
        initialVelocity = Vec3d.ZERO;
        detectionRange = -1;
    }
    
    public LightSpearEntity(World world, LivingEntity owner, float damageMod, float lifespanMod) {
        this(world, owner, Optional.empty(), damageMod, lifespanMod);
    }
    
    public LightSpearEntity(World world, LivingEntity owner, Optional<Entity> target, float damageMod, float lifespanMod) {
        super(SpectrumEntityTypes.LIGHT_SPEAR, world);
        target.ifPresent(this::setTarget);
        this.setOwner(owner);
        
        var random = world.getRandom();
        
        scaleOffset = random.nextFloat() * 0.5F + 0.5F;
        rotationOffset = random.nextFloat() * 360 - 180;
        maxAge = (long) (DEFAULT_MAX_AGE * lifespanMod);
        damage = DEFAULT_DAMAGE * damageMod;
    }
    
    @Override
    public Identifier getTexture() {
        return SpectrumCommon.locate("textures/entity/projectile/light_spear.png");
    }
    
    public static void summonBarrage(World world, LivingEntity user, @Nullable Entity target) {
        summonBarrageInternal(world, user, () -> new LightSpearEntity(world, user, Optional.ofNullable(target), 0.5F, 1.0F));
    }
    
}
