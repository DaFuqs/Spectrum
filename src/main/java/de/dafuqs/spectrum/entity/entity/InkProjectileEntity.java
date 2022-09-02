package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.entity.SpectrumEntityTypes;
import de.dafuqs.spectrum.helpers.BlockVariantHelper;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import de.dafuqs.spectrum.sound.InkProjectileSoundInstance;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class InkProjectileEntity extends ProjectileEntity {
	
	private static final int COLOR_SPLAT_RANGE = 2;
	private static final int SPELL_POTENCY = 2;
	private static final float DAMAGE_PER_POTENCY = 0.5F;
	
	private static final TrackedData<Integer> COLOR = DataTracker.registerData(InkProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	protected int life;
	
	public InkProjectileEntity(EntityType<InkProjectileEntity> type, World world) {
		super(type, world);
		if(world.isClient) {
			InkProjectileSoundInstance.startSoundInstance(this);
		}
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
	
	public DyeColor getDyeColor() {
		return DyeColor.byId(this.dataTracker.get(COLOR));
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
			DyeColor dyeColor = DyeColor.byId(colorOrdinal);
			Vec3f inkColor = InkColor.of(dyeColor).getColor();
			for(int j = 0; j < amount; ++j) {
				this.world.addParticle(SpectrumParticleTypes.getCraftingParticle(dyeColor), this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), inkColor.getX(), inkColor.getY(), inkColor.getZ());
			}
		}
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		
		Entity entity = entityHitResult.getEntity();
		
		ColorHelper.tryColorEntity(null, entity, getDyeColor());
		
		float velocity = (float)this.getVelocity().length();
		int damage = MathHelper.ceil(MathHelper.clamp((double)velocity * DAMAGE_PER_POTENCY * SPELL_POTENCY, 0.0D, 2.147483647E9D));
		
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
		
		if (entity.damage(damageSource, (float)damage)) {
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
						SpectrumAdvancementCriteria.KILLED_BY_INK_PROJECTILE.trigger(serverPlayerEntity, Arrays.asList(entity));
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
			DyeColor dyeColor = DyeColor.byId(colorOrdinal);
			
			for (BlockPos blockPos : BlockPos.iterateOutwards(blockHitResult.getBlockPos(), COLOR_SPLAT_RANGE, COLOR_SPLAT_RANGE, COLOR_SPLAT_RANGE)) {
				Block coloredBlock = BlockVariantHelper.getCursedBlockColorVariant(this.world, blockPos, dyeColor);
				if (coloredBlock != Blocks.AIR) {
					this.world.setBlockState(blockPos, coloredBlock.getDefaultState());
				}
			}
			
			affectEntitiesInRange(this.getOwner());
			
			// TODO: uncomment this when all 16 ink effects are finished
			// InkSpellEffect.trigger(InkColor.of(dyeColor), this.world, blockHitResult.getPos(), SPELL_POTENCY);
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
			
			// TODO: this is a dummy effect
			Vec3d vec3d = this.getVelocity().multiply(1.0D, 0.0D, 1.0D).normalize().multiply((double) 3 * 0.6D);
			if (vec3d.lengthSquared() > 0.0D) {
				entity.addVelocity(vec3d.x, 0.1D, vec3d.z);
			}
			
			affectEntitiesInRange(this.getOwner());
			
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
	
	public void affectEntitiesInRange(Entity attacker) {
		this.world.emitGameEvent(this, GameEvent.PROJECTILE_LAND, new BlockPos(this.getPos().x, this.getPos().y, this.getPos().z));
		
		double posX = this.getPos().x;
		double posY = this.getPos().y;
		double posZ = this.getPos().z;
		
		float q = SPELL_POTENCY * 2.0F;
		double k = MathHelper.floor(posX - (double) q - 1.0D);
		double l = MathHelper.floor(posX + (double) q + 1.0D);
		int r = MathHelper.floor(posY - (double) q - 1.0D);
		int s = MathHelper.floor(posY + (double) q + 1.0D);
		int t = MathHelper.floor(posZ - (double) q - 1.0D);
		int u = MathHelper.floor(posZ + (double) q + 1.0D);
		List<Entity> list = this.world.getOtherEntities(this, new Box(k, r, t, l, s, u));
		Vec3d vec3d = new Vec3d(posX, posY, posZ);
		
		for (Entity entity : list) {
			ColorHelper.tryColorEntity(null, entity, getDyeColor());
			
			if (!entity.isImmuneToExplosion()) {
				double w = Math.sqrt(entity.squaredDistanceTo(vec3d)) / (double) q;
				if (w <= 1.0D) {
					double x = entity.getX() - posX;
					double y = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - posY;
					double z = entity.getZ() - posZ;
					double aa = Math.sqrt(x * x + y * y + z * z);
					if (aa != 0.0D) {
						x /= aa;
						y /= aa;
						z /= aa;
						double ab = Explosion.getExposure(vec3d, entity);
						double ac = (1.0D - w) * ab;
						
						//float damage = (float) ((int) ((ac * ac + ac) / 2.0D * (double) q + 1.0D));
						//entity.damage(SpectrumDamageSources.inkProjectile(this, attacker), damage);
						
						double ad = ac;
						if (entity instanceof LivingEntity) {
							ad = ProtectionEnchantment.transformExplosionKnockback((LivingEntity) entity, ac);
						}
						
						entity.setVelocity(entity.getVelocity().add(x * ad, y * ad, z * ad));
					}
				}
			}
		}
	}
	
}
