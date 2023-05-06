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

public class LightShardEntity extends LightShardBaseEntity {
    
    public LightShardEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public LightShardEntity(World world, LivingEntity owner, Optional<Entity> target, float damageMod, float lifespanMod) {
        super(SpectrumEntityTypes.LIGHT_SHARD, world, owner, target);
    
        this.detectionRange = 48;
        this.maxAge = (int) ((DEFAULT_MAX_AGE + MathHelper.nextGaussian(world.getRandom(), 10, 7)) * lifespanMod);
        this.damage = 4 * damageMod;
    }
    
    public static void summonBarrage(World world, LivingEntity user, @Nullable Entity target) {
        summonBarrageInternal(world, user, () -> new LightShardEntity(world, user, Optional.ofNullable(target), 0.5F, 1.0F));
    }
    
    public Identifier getTexture() {
        return SpectrumCommon.locate("textures/entity/projectile/light_shard.png");
    }
    
}
