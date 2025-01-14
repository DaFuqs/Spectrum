package de.dafuqs.spectrum.api.interaction;

import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.predicate.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface OmniAcceleratorProjectile {
	
	List<Pair<ItemPredicate, OmniAcceleratorProjectile>> PROJECTILES = new ArrayList<>();
	
	OmniAcceleratorProjectile DEFAULT = (stack, shooter, world) -> {
		ItemProjectileEntity itemProjectileEntity = new ItemProjectileEntity(world, shooter);
		itemProjectileEntity.setItem(stack);
		itemProjectileEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.5F, 0.5F);
		world.spawnEntity(itemProjectileEntity);
		return itemProjectileEntity;
	};
	
	static void register(OmniAcceleratorProjectile behavior, ItemPredicate predicate) {
		PROJECTILES.add(new Pair<>(predicate, behavior));
	}
	
	static void register(OmniAcceleratorProjectile behavior, ItemConvertible... items) {
		PROJECTILES.add(new Pair<>(ItemPredicate.Builder.create().items(items).build(), behavior));
	}
	
	static void register(OmniAcceleratorProjectile behavior, TagKey<Item> tag) {
		PROJECTILES.add(new Pair<>(ItemPredicate.Builder.create().tag(tag).build(), behavior));
	}
	
	static OmniAcceleratorProjectile get(ItemStack stack) {
		for (Pair<ItemPredicate, OmniAcceleratorProjectile> entry : PROJECTILES) {
			if (entry.getLeft().test(stack)) {
				return entry.getRight();
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
	Entity createProjectile(ItemStack stack, LivingEntity shooter, World world);
	
	/**
	 * The sound effect to play when the projectile has been fired successfully
	 *
	 * @return The sound effect to play when the projectile has been fired successfully
	 */
	default SoundEvent getSoundEffect() {
		return SpectrumSoundEvents.OMNI_ACCELERATOR_SHOOT;
	}
	
	static void setVelocity(Entity projectile, double x, double y, double z, float speed, float divergence) {
		Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(
				projectile.getWorld().getRandom().nextTriangular(0.0, 0.0172275 * (double) divergence),
				projectile.getWorld().getRandom().nextTriangular(0.0, 0.0172275 * (double) divergence),
				projectile.getWorld().getRandom().nextTriangular(0.0, 0.0172275 * (double) divergence)
		).multiply(speed);
		projectile.setVelocity(vec3d);
		double d = vec3d.horizontalLength();
		projectile.setYaw((float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
		projectile.setPitch((float) (MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
		projectile.prevYaw = projectile.getYaw();
		projectile.prevPitch = projectile.getPitch();
	}
	
	static void setVelocity(Entity projectile, Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
		float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		float g = -MathHelper.sin((pitch + roll) * 0.017453292F);
		float h = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
		setVelocity(projectile, f, g, h, speed, divergence);
		Vec3d vec3d = shooter.getVelocity();
		projectile.setVelocity(projectile.getVelocity().add(vec3d.x, shooter.isOnGround() ? 0.0 : vec3d.y, vec3d.z));
	}
	
}
