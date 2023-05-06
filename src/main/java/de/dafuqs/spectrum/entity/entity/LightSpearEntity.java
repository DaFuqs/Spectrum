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
    }
    
    public LightSpearEntity(World world, LivingEntity owner, Optional<Entity> target, float damageMod, float lifespanMod) {
        super(SpectrumEntityTypes.LIGHT_SPEAR, world, owner, target);
		target.ifPresent(this::setTarget);
        this.setOwner(owner);
    
        this.detectionRange = -1; // needs a target
        this.maxAge = (int) ((DEFAULT_MAX_AGE + MathHelper.nextGaussian(world.getRandom(), 10, 7)) * lifespanMod);
        this.damage = 8f * damageMod;
    }
    
    @Override
    public Identifier getTexture() {
        return SpectrumCommon.locate("textures/entity/projectile/light_spear.png");
    }
    
    public static void summonBarrage(World world, LivingEntity user, @Nullable Entity target) {
        summonBarrageInternal(world, user, () -> new LightSpearEntity(world, user, Optional.ofNullable(target), 0.5F, 1.0F));
    }
    
}
