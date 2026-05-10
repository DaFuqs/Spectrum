package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.entity.*;
import net.minecraft.commands.arguments.*;
import net.minecraft.resources.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;

import java.util.function.*;

public class LightSpearEntity extends LightShardBaseEntity {
	
	public LightSpearEntity(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
    }
	
	public LightSpearEntity(Level world, @Nullable LivingEntity owner, float damage, int lifeSpanTicks) {
		super(SpectrumEntityTypes.LIGHT_SPEAR, world, owner, 48, damage, lifeSpanTicks);
	}

    @Override
    public void tick() {
        super.tick();
		
		if (target != null) this.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
	}

	@Override
	public ResourceLocation getTextureLocation() {
		return SpectrumCommon.locate("textures/entity/projectile/light_spear.png");
	}
	
	public static void summonBarrage(Level world, @Nullable LivingEntity user, @Nullable LivingEntity target, Predicate<LivingEntity> targetPredicate, Vec3 position, IntProvider count) {
		summonBarrageInternal(world, user, () -> new LightSpearEntity(world, user, 12.0F, 200), target, targetPredicate, position, count);
	}

}
