package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.blocks.shooting_star.*;
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
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				PotionEntity potionEntity = new PotionEntity(world, shooter);
				potionEntity.setItem(stack);
				potionEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.5F, 1.0F);
				world.spawnEntity(potionEntity);
				return potionEntity;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_SPLASH_POTION_THROW;
			}
		}, Items.SPLASH_POTION);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				PotionEntity potionEntity = new PotionEntity(world, shooter);
				potionEntity.setItem(stack);
				potionEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.5F, 1.0F);
				world.spawnEntity(potionEntity);
				return potionEntity;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_LINGERING_POTION_THROW;
			}
		}, Items.LINGERING_POTION);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				EnderPearlEntity enderPearlEntity = new EnderPearlEntity(world, shooter);
				enderPearlEntity.setItem(stack);
				enderPearlEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.5F, 1.0F);
				world.spawnEntity(enderPearlEntity);
				return enderPearlEntity;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_ENDER_PEARL_THROW;
			}
		}, Items.ENDER_PEARL);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				if (stack.getItem() instanceof ArrowItem arrowItem) {
					PersistentProjectileEntity arrowEntity = arrowItem.createArrow(world, stack, shooter);
					arrowEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.5F, 1.0F);
					world.spawnEntity(arrowEntity);
					return arrowEntity;
				}
				return null;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_ARROW_SHOOT;
			}
		}, ItemTags.ARROWS);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				SnowballEntity snowballEntity = new SnowballEntity(world, shooter);
				snowballEntity.setItem(stack);
				snowballEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.5F, 1.0F);
				world.spawnEntity(snowballEntity);
				return snowballEntity;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_SNOWBALL_THROW;
			}
		}, Items.SNOWBALL);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				EggEntity eggEntity = new EggEntity(world, shooter);
				eggEntity.setItem(stack);
				eggEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.5F, 1.0F);
				world.spawnEntity(eggEntity);
				return eggEntity;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_EGG_THROW;
			}
		}, Items.EGG);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				Vec3d pos = shooter.getEyePos();
				TntEntity tntEntity = new TntEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, shooter);
				OmniAcceleratorProjectile.setVelocity(tntEntity, shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.5F, 1.0F);
				if (world.spawnEntity(tntEntity)) {
					world.emitGameEvent(shooter, GameEvent.PRIME_FUSE, pos);
					return tntEntity;
				}
				return null;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_TNT_PRIMED;
			}
		}, Items.TNT);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				BlockFlooderProjectile blockFlooderProjectile = new BlockFlooderProjectile(world, shooter);
				blockFlooderProjectile.setItem(stack);
				blockFlooderProjectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 2.5F, 1.0F);
				world.spawnEntity(blockFlooderProjectile);
				return blockFlooderProjectile;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SpectrumSoundEvents.ENTITY_BLOCK_FLOODER_THROW;
			}
		}, SpectrumItems.BLOCK_FLOODER);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				ParametricMiningDeviceEntity entity = new ParametricMiningDeviceEntity(world, shooter);
				entity.setItem(stack);
				entity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0, 2.5F, 0F);
				world.spawnEntity(entity);
				return entity;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SpectrumSoundEvents.BLOCK_PARAMETRIC_MINING_DEVICE_THROWN;
			}
		}, SpectrumBlocks.PARAMETRIC_MINING_DEVICE);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(world, stack, shooter);
				fireworkRocketEntity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0, 2.5F, 0F);
				world.spawnEntity(fireworkRocketEntity);
				return fireworkRocketEntity;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH;
			}
		}, Items.FIREWORK_ROCKET);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				ShootingStarEntity shootingStarEntity = ((ShootingStarItem) stack.getItem()).getEntityForStack(world, shooter.getEyePos(), stack);
				OmniAcceleratorProjectile.setVelocity(shootingStarEntity, shooter, shooter.getPitch(), shooter.getYaw(), 0, 2.5F, 0F);
				world.spawnEntity(shootingStarEntity);
				
				shootingStarEntity.noClip = true;
				shootingStarEntity.move(MovementType.SELF, shootingStarEntity.getVelocity()); // leave the owner
				shootingStarEntity.move(MovementType.SELF, shootingStarEntity.getVelocity()); // leave the owner
				shootingStarEntity.noClip = false;
				
				return shootingStarEntity;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SpectrumSoundEvents.SHOOTING_STAR_CRACKER;
			}
		}, SpectrumBlocks.GLISTERING_SHOOTING_STAR, SpectrumBlocks.FIERY_SHOOTING_STAR, SpectrumBlocks.COLORFUL_SHOOTING_STAR, SpectrumBlocks.PRISTINE_SHOOTING_STAR, SpectrumBlocks.GEMSTONE_SHOOTING_STAR);
		
		OmniAcceleratorProjectile.register(new OmniAcceleratorProjectile() {
			@Override
			public Entity createProjectile(ItemStack stack, LivingEntity shooter, World world) {
				Vec3d pos = shooter.getEyePos();
				
				if (stack.getItem() instanceof BlockItem blockItem) {
					FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, blockItem.getBlock().getDefaultState());
					OmniAcceleratorProjectile.setVelocity(fallingBlockEntity, shooter, shooter.getPitch(), shooter.getYaw(), 0, 2.5F, 0F);
					world.spawnEntity(fallingBlockEntity);
					world.spawnEntity(fallingBlockEntity);
					return fallingBlockEntity;
				}
				
				return null;
			}
			
			@Override
			public SoundEvent getSoundEffect() {
				return SoundEvents.ENTITY_SPLASH_POTION_THROW;
			}
		}, ItemTags.ANVIL);
	}
	
}
