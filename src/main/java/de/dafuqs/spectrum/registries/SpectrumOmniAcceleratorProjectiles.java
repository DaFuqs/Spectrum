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
import net.minecraft.world.*;
import net.minecraft.world.event.*;

public class SpectrumOmniAcceleratorProjectiles {

	public static void register() {
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public boolean fireProjectile(ItemStack stack, LivingEntity shooter, World world) {
				EnderPearlEntity enderPearlEntity = new EnderPearlEntity(world, shooter);
				enderPearlEntity.setItem(stack);
				enderPearlEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.0F, 1.0F);
				return world.spawnEntity(enderPearlEntity);
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_ENDER_PEARL_THROW;
			}
		}, Items.ENDER_PEARL);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public boolean fireProjectile(ItemStack stack, LivingEntity shooter, World world) {
				if (stack.getItem() instanceof ArrowItem arrowItem) {
					PersistentProjectileEntity arrowEntity = arrowItem.createArrow(world, stack, shooter);
					arrowEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.0F, 1.0F);
					return world.spawnEntity(arrowEntity);
				}
				return true;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_ARROW_SHOOT;
			}
		}, ItemTags.ARROWS);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public boolean fireProjectile(ItemStack stack, LivingEntity shooter, World world) {
				SnowballEntity snowballEntity = new SnowballEntity(world, shooter);
				snowballEntity.setItem(stack);
				snowballEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.0F, 1.0F);
				return world.spawnEntity(snowballEntity);
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_SNOWBALL_THROW;
			}
		}, Items.SNOWBALL);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public boolean fireProjectile(ItemStack stack, LivingEntity shooter, World world) {
				EggEntity eggEntity = new EggEntity(world, shooter);
				eggEntity.setItem(stack);
				eggEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.0F, 1.0F);
				return world.spawnEntity(eggEntity);
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_EGG_THROW;
			}
		}, Items.EGG);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public boolean fireProjectile(ItemStack stack, LivingEntity shooter, World world) {
				Vec3d pos = shooter.getPos();
				TntEntity tntEntity = new TntEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, shooter);
				OmniAcceleratorProjectile.setVelocity(tntEntity, shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.0F, 1.0F);
				if (world.spawnEntity(tntEntity)) {
					world.emitGameEvent(shooter, GameEvent.PRIME_FUSE, pos);
					return true;
				}
				return false;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_TNT_PRIMED;
			}
		}, Items.TNT);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public boolean fireProjectile(ItemStack stack, LivingEntity shooter, World world) {
				BlockFlooderProjectile blockFlooderProjectile = new BlockFlooderProjectile(world, shooter);
				blockFlooderProjectile.setItem(stack);
				blockFlooderProjectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 1.5F, 1.0F);
				return world.spawnEntity(blockFlooderProjectile);
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_SNOWBALL_THROW; // TODO: create dedicated sound event
			}
		}, SpectrumItems.BLOCK_FLOODER);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public boolean fireProjectile(ItemStack stack, LivingEntity shooter, World world) {
				ParametricMiningDeviceEntity entity = new ParametricMiningDeviceEntity(world, shooter);
				entity.setItem(stack);
				entity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0, 1.5F, 0F);
				return world.spawnEntity(entity);
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SpectrumSoundEvents.BLOCK_PARAMETRIC_MINING_DEVICE_THROWN;
			}
		}, SpectrumBlocks.PARAMETRIC_MINING_DEVICE.asItem());
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public boolean fireProjectile(ItemStack stack, LivingEntity shooter, World world) {
				FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, stack, shooter);
				fireworkRocketEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0, 1.5F, 0F);
				return world.spawnEntity(fireworkRocketEntity);
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH;
			}
		}, Items.FIREWORK_ROCKET);
	}

}
