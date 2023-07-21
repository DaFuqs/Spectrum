package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class LightShardEntity extends LightShardBaseEntity {
	
	public LightShardEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public LightShardEntity(World world, LivingEntity owner, Optional<Entity> target, float damageMod, float lifeSpanTicks) {
		super(SpectrumEntityTypes.LIGHT_SHARD, world, owner, target, 48, damageMod, lifeSpanTicks);
	}
	
	public static void summonBarrage(World world, LivingEntity user, @Nullable Entity target) {
		summonBarrage(world, user, target, user.getEyePos(), DEFAULT_COUNT_PROVIDER);
	}
	
	public static void summonBarrage(World world, LivingEntity user, @Nullable Entity target, Vec3d position, IntProvider count) {
		summonBarrageInternal(world, user, () -> new LightShardEntity(world, user, Optional.ofNullable(target), 0.5F, 200), position, count);
	}
	
	@Override
	public Identifier getTexture() {
		return SpectrumCommon.locate("textures/entity/projectile/light_shard.png");
	}
	
}
