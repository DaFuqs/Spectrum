package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.command.argument.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class LightSpearEntity extends LightShardBaseEntity {
    
    public LightSpearEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
	
	public LightSpearEntity(World world, LivingEntity owner, float damage, int lifeSpanTicks) {
		super(SpectrumEntityTypes.LIGHT_SPEAR, world, owner, 48, damage, lifeSpanTicks);
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
	
	public static void summonBarrage(World world, @Nullable LivingEntity user, @Nullable LivingEntity target, Predicate<LivingEntity> targetPredicate, Vec3d position, IntProvider count) {
		summonBarrageInternal(world, user, () -> new LightSpearEntity(world, user, 12.0F, 200), target, targetPredicate, position, count);
	}

}
