package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class InkProjectileEntity extends ProjectileEntity {
	
	private static final int COLOR_SPLAT_RANGE = 2;
	private static final TrackedData<Integer> COLOR = DataTracker.registerData(ArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected int life;
	protected int damage = 2;
	
	public InkProjectileEntity(EntityType<InkProjectileEntity> type, World world) {
		super(type, world);
	}
	
	public InkProjectileEntity(EntityType<InkProjectileEntity> type, double x, double y, double z, World world) {
		this(type, world);
		this.refreshPositionAndAngles(x, y, z, this.getYaw(), this.getPitch());
		this.refreshPosition();
	}
	
	public InkProjectileEntity(World world, LivingEntity owner) {
		this(SpectrumEntityTypes.INK_PROJECTILE, owner.getX(), owner.getEyeY() - 0.10000000149011612D, owner.getZ(), world);
		this.setOwner(owner);
		this.setRotation(owner.getYaw(), owner.getPitch());
	}
	
	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(COLOR, -1);
	}
	
	public int getColor() {
		return this.dataTracker.get(COLOR);
	}
	
	public void setColor(InkColor inkColor) {
		this.dataTracker.set(COLOR, inkColor.getDyeColor().getId());
	}
	
	protected void setColor(int color) {
		this.dataTracker.set(COLOR, color);
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		
		int color = this.getColor();
		if (color != -1) {
			nbt.putInt("Color", color);
		}
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);

		if (nbt.contains("Color", 99)) {
			this.setColor(nbt.getInt("Color"));
		}
	}
	
	// TODO
	@Override
	public void tick() {
		super.tick();
		
		this.spawnParticles(1);
		
		boolean noClip = this.isNoClip();
		Vec3d thisVelocity = this.getVelocity();
		if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
			double d = thisVelocity.horizontalLength();
			this.setYaw((float)(MathHelper.atan2(thisVelocity.x, thisVelocity.z) * 57.2957763671875D));
			this.setPitch((float)(MathHelper.atan2(thisVelocity.y, d) * 57.2957763671875D));
			this.prevYaw = this.getYaw();
			this.prevPitch = this.getPitch();
		}
		
		this.age();
		
		Vec3d vec3d2;
		Vec3d thisPos = this.getPos();
		vec3d2 = thisPos.add(thisVelocity);
		HitResult hitResult = this.world.raycast(new RaycastContext(thisPos, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
		if ((hitResult).getType() != HitResult.Type.MISS) {
			vec3d2 = (hitResult).getPos();
		}
		
		if(!this.isRemoved()) {
			EntityHitResult entityHitResult = this.getEntityCollision(thisPos, vec3d2);
			if (entityHitResult != null) {
				hitResult = entityHitResult;
			}
			
			if (hitResult.getType() == HitResult.Type.ENTITY) {
				Entity entity = ((EntityHitResult)hitResult).getEntity();
				Entity entity2 = this.getOwner();
				if (entity instanceof PlayerEntity && entity2 instanceof PlayerEntity && !((PlayerEntity)entity2).shouldDamagePlayer((PlayerEntity)entity)) {
					hitResult = null;
				}
			}
			
			if (hitResult != null && !noClip) {
				this.onCollision(hitResult);
				this.velocityDirty = true;
			}
		}
		
		thisVelocity = this.getVelocity();
		double velocityX = thisVelocity.x;
		double velocityY = thisVelocity.y;
		double velocityZ = thisVelocity.z;
		
		double h = this.getX() + velocityX;
		double j = this.getY() + velocityY;
		double k = this.getZ() + velocityZ;
		double l = thisVelocity.horizontalLength();
		if (noClip) {
			this.setYaw((float)(MathHelper.atan2(-velocityX, -velocityZ) * 57.2957763671875D));
		} else {
			this.setYaw((float)(MathHelper.atan2(velocityX, velocityZ) * 57.2957763671875D));
		}
		
		this.setPitch((float)(MathHelper.atan2(velocityY, l) * 57.2957763671875D));
		this.setPitch(updateRotation(this.prevPitch, this.getPitch()));
		this.setYaw(updateRotation(this.prevYaw, this.getYaw()));
		
		if (this.isTouchingWater()) {
			for(int o = 0; o < 4; ++o) {
				this.world.addParticle(ParticleTypes.BUBBLE, h - velocityX * 0.25D, j - velocityY * 0.25D, k - velocityZ * 0.25D, velocityX, velocityY, velocityZ);
			}
		}
		
		this.setPosition(h, j, k);
		this.checkBlockCollision();
	}
	
	protected void age() {
		++this.life;
		if (this.life >= 200) {
			this.discard();
		}
		
	}
	
	public boolean isNoClip() {
		if (!this.world.isClient) {
			return this.noClip;
		} else {
			return true;
		}
	}
	
	private void spawnParticles(int amount) {
		int colorOrdinal = this.getColor();
		if (colorOrdinal != -1 && amount > 0) {
			Vec3f inkColor = InkColor.of(DyeColor.byId(colorOrdinal)).getColor();
			for(int j = 0; j < amount; ++j) {
				this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), inkColor.getX(), inkColor.getY(), inkColor.getZ());
			}
		}
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		Entity entity = entityHitResult.getEntity();
		float f = (float)this.getVelocity().length();
		int i = MathHelper.ceil(MathHelper.clamp((double)f * this.damage, 0.0D, 2.147483647E9D));
		
		Entity entity2 = this.getOwner();
		DamageSource damageSource;
		if (entity2 == null) {
			damageSource = SpectrumDamageSources.inkProjectile(this, this);
		} else {
			damageSource = SpectrumDamageSources.inkProjectile(this, entity2);
			if (entity2 instanceof LivingEntity) {
				((LivingEntity)entity2).onAttacking(entity);
			}
		}
		
		if (entity.damage(damageSource, (float)i)) {
			if (entity instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity)entity;
				
				if (!this.world.isClient && entity2 instanceof LivingEntity) {
					EnchantmentHelper.onUserDamaged(livingEntity, entity2);
					EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity);
				}
				
				this.onHit(livingEntity);
				
				if (livingEntity != entity2 && livingEntity instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
					((ServerPlayerEntity)entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0F));
				}
				
				if (!this.world.isClient && entity2 instanceof ServerPlayerEntity) {
					ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)entity2;
					if (!entity.isAlive()) {
						Criteria.KILLED_BY_CROSSBOW.trigger(serverPlayerEntity, Arrays.asList(entity));
					}
				}
			}
			
			this.playSound(this.getHitSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
			this.discard();
		} else {
			this.setVelocity(this.getVelocity().multiply(-0.1D));
			this.setYaw(this.getYaw() + 180.0F);
			this.prevYaw += 180.0F;
			if (!this.world.isClient && this.getVelocity().lengthSquared() < 1.0E-7D) {
				this.discard();
			}
		}
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		
		Vec3d vec3d = blockHitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
		this.setVelocity(vec3d);
		Vec3d vec3d2 = vec3d.normalize().multiply(0.05000000074505806D);
		this.setPos(this.getX() - vec3d2.x, this.getY() - vec3d2.y, this.getZ() - vec3d2.z);
		this.playSound(this.getHitSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
		
		int colorOrdinal = this.getColor();
		if (colorOrdinal != -1) {
			for (BlockPos blockPos : BlockPos.iterateOutwards(blockHitResult.getBlockPos(), COLOR_SPLAT_RANGE, COLOR_SPLAT_RANGE, COLOR_SPLAT_RANGE)) {
				Block coloredBlock = ColorHelper.getCursedBlockColorVariant(this.world, blockPos, DyeColor.byId(colorOrdinal));
				if (coloredBlock != Blocks.AIR) {
					this.world.setBlockState(blockPos, coloredBlock.getDefaultState());
				}
			}
			
			for (int i = 0; i < 10; i++) {
				SpectrumS2CPacketSender.playParticleWithExactOffsetAndVelocity((ServerWorld) this.world, this.getPos(),
						SpectrumParticleTypes.getCraftingParticle(DyeColor.byId(colorOrdinal)), 10,
						Vec3d.ZERO,
						new Vec3d(-this.getVelocity().x * 3, -this.getVelocity().y * 3, -this.getVelocity().z * 3)
				);
			}
		}
		
		this.discard();
	}
	
	protected SoundEvent getHitSound() {
		return SpectrumSoundEvents.INK_PROJECTILE_HIT;
	}
	
	@Nullable
	protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
		return ProjectileUtil.getEntityCollision(this.world, this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit);
	}
	
	protected void onHit(LivingEntity target) {
		int colorOrdinal = this.getColor();
		if (colorOrdinal != -1) {
			//InkColor.all().get(colorOrdinal);
			
			Entity entity = target; //this.getEffectCause();
			
			Vec3d vec3d = this.getVelocity().multiply(1.0D, 0.0D, 1.0D).normalize().multiply((double) 3 * 0.6D);
			if (vec3d.lengthSquared() > 0.0D) {
				entity.addVelocity(vec3d.x, 0.1D, vec3d.z);
			} // TODO: this is a dummy effect
			
			
			/*Iterator var3 = this.potion.getEffects().iterator();
			
			StatusEffectInstance statusEffectInstance;
			while (var3.hasNext()) {
				statusEffectInstance = (StatusEffectInstance) var3.next();
				target.addStatusEffect(new StatusEffectInstance(statusEffectInstance.getEffectType(), Math.max(statusEffectInstance.getDuration() / 8, 1), statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()), entity);
			}
			
			if (!this.effects.isEmpty()) {
				var3 = this.effects.iterator();
				
				while (var3.hasNext()) {
					statusEffectInstance = (StatusEffectInstance) var3.next();
					target.addStatusEffect(statusEffectInstance, entity);
				}
			}*/
		}
		
		this.discard();
	}

	
}
