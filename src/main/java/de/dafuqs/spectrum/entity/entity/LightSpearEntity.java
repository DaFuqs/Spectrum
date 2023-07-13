package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.command.argument.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class LightSpearEntity extends LightShardBaseEntity {
    
    public LightSpearEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public LightSpearEntity(World world, LivingEntity owner, Optional<Entity> target, float damage, int lifeSpanTicks) {
        super(SpectrumEntityTypes.LIGHT_SPEAR, world, owner, target, -1, damage, lifeSpanTicks);
    }
    
    @Override
    public void tick() {
        super.tick();
        
        targetEntity.ifPresent(entity -> this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.getPos()));
    }
    
    @Override
    public Identifier getTexture() {
        return SpectrumCommon.locate("textures/entity/projectile/light_spear.png");
    }
    
    public static void summonBarrage(World world, LivingEntity user, @Nullable Entity target) {
        summonBarrageInternal(world, user, () -> new LightSpearEntity(world, user, Optional.ofNullable(target), 12.0F, 200));
    }
    
}
