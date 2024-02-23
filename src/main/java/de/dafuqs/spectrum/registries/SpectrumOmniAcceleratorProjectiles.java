package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;

public class SpectrumOmniAcceleratorProjectiles {

	public static void register() {
		OmniAcceleratorProjectile.register((stack, shooter, world) -> {
				EnderPearlEntity enderPearlEntity = new EnderPearlEntity(world, shooter);
				enderPearlEntity.setItem(stack);
				enderPearlEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.0F, 1.0F);
				return world.spawnEntity(enderPearlEntity);
		}, Items.ENDER_PEARL);

		OmniAcceleratorProjectile.register((stack, shooter, world) -> {
			if(stack.getItem() instanceof ArrowItem arrowItem) {
				PersistentProjectileEntity arrowEntity = arrowItem.createArrow(world, stack, shooter);
				arrowEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.0F, 1.0F);
				return world.spawnEntity(arrowEntity);
			}
			return false;
		}, ItemTags.ARROWS);

		OmniAcceleratorProjectile.register((stack, shooter, world) -> {
			SnowballEntity snowballEntity = new SnowballEntity(world, shooter);
			snowballEntity.setItem(stack);
			snowballEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.0F, 1.0F);
			return world.spawnEntity(snowballEntity);
		}, Items.SNOWBALL);

		OmniAcceleratorProjectile.register((stack, shooter, world) -> {
			EggEntity eggEntity = new EggEntity(world, shooter);
			eggEntity.setItem(stack);
			eggEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.0F, 1.0F);
			return world.spawnEntity(eggEntity);
		}, Items.EGG);

		OmniAcceleratorProjectile.register((stack, shooter, world) -> {
			Vec3d pos = shooter.getPos();
			TntEntity tntEntity = new TntEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, shooter);
			OmniAcceleratorProjectile.setVelocity(tntEntity, shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.0F, 1.0F);
			if(world.spawnEntity(tntEntity)) {
				world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(shooter, GameEvent.PRIME_FUSE, pos);
				return true;
			}
			return false;
		}, Items.TNT);

		OmniAcceleratorProjectile.register((stack, shooter, world) -> {
			BlockFlooderProjectile blockFlooderProjectile = new BlockFlooderProjectile(world, shooter);
			blockFlooderProjectile.setItem(stack);
			blockFlooderProjectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 1.5F, 1.0F);
			return world.spawnEntity(blockFlooderProjectile);
		}, SpectrumItems.BLOCK_FLOODER);

		OmniAcceleratorProjectile.register((stack, shooter, world) -> {
			ParametricMiningDeviceEntity entity = new ParametricMiningDeviceEntity(world, shooter);
			entity.setItem(stack);
			entity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0, 1.5F, 0F);
			return world.spawnEntity(entity);
		});
	}

}
