package de.dafuqs.spectrum.api.interaction;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface OmniAcceleratorProjectile {
	
	List<Tuple<ItemPredicate, OmniAcceleratorProjectile>> PROJECTILES = new ArrayList<>();
	
	OmniAcceleratorProjectile DEFAULT = (stack, shooter, world, shotFrom) -> {
		ItemProjectileEntity itemProjectileEntity = new ItemProjectileEntity(world, shooter);
		itemProjectileEntity.setItem(stack);
		itemProjectileEntity.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 2.5F, 0.5F);
		world.addFreshEntity(itemProjectileEntity);
		return itemProjectileEntity;
	};
	
	static void register(OmniAcceleratorProjectile behavior, ItemPredicate predicate) {
		PROJECTILES.add(new Tuple<>(predicate, behavior));
	}
	
	static void register(OmniAcceleratorProjectile behavior, ItemLike... items) {
		PROJECTILES.add(new Tuple<>(ItemPredicate.Builder.item().of(items).build(), behavior));
	}
	
	static void register(OmniAcceleratorProjectile behavior, TagKey<Item> tag) {
		PROJECTILES.add(new Tuple<>(ItemPredicate.Builder.item().of(tag).build(), behavior));
	}
	
	static OmniAcceleratorProjectile get(ItemStack stack) {
		for (Tuple<ItemPredicate, OmniAcceleratorProjectile> entry : PROJECTILES) {
			if (entry.getA().test(stack)) {
				return entry.getB();
			}
		}
		return DEFAULT;
	}
	
	/**
	 * Invoked when an entity uses the Omni Accelerator to fire a projectile.
	 * Implement your custom projectile spawning behavior here. Only triggers server side.
	 *
	 * @param stack   The stack used as projectile (always count of 1)
	 * @param shooter The entity shooting the Omni Accelerator
	 * @param world   The World
	 * @return The created projectile. If not null, the fired stack will be decremented and the getSoundEffect() sound will play
	 */
	@Nullable
	Entity createProjectile(ItemStack stack, LivingEntity shooter, Level world, ItemStack shotFrom);
	
	/**
	 * The sound effect to play when the projectile has been fired successfully
	 *
	 * @return The sound effect to play when the projectile has been fired successfully
	 */
	default SoundEvent getSoundEffect() {
		return SpectrumSoundEvents.OMNI_ACCELERATOR_SHOOT;
	}
	
	static void setVelocity(Entity projectile, double x, double y, double z, float speed, float divergence) {
		Vec3 vec3d = (new Vec3(x, y, z)).normalize().add(
				projectile.level().getRandom().triangle(0.0, 0.0172275 * (double) divergence),
				projectile.level().getRandom().triangle(0.0, 0.0172275 * (double) divergence),
				projectile.level().getRandom().triangle(0.0, 0.0172275 * (double) divergence)
		).scale(speed);
		projectile.setDeltaMovement(vec3d);
		double d = vec3d.horizontalDistance();
		projectile.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
		projectile.setXRot((float) (Mth.atan2(vec3d.y, d) * 57.2957763671875));
		projectile.yRotO = projectile.getYRot();
		projectile.xRotO = projectile.getXRot();
	}
	
	static void setVelocity(Entity projectile, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
		float f = -Mth.sin(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
		float g = -Mth.sin((pitch + roll) * 0.017453292F);
		float h = Mth.cos(yaw * 0.017453292F) * Mth.cos(pitch * 0.017453292F);
		setVelocity(projectile, f, g, h, speed, divergence);
		Vec3 vec3d = shooter.getDeltaMovement();
		projectile.setDeltaMovement(projectile.getDeltaMovement().add(vec3d.x, shooter.onGround() ? 0.0 : vec3d.y, vec3d.z));
	}
	
}
