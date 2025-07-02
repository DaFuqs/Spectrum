package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class InkProjectileEntity extends MagicProjectileEntity {

	private static final int COLOR_SPLAT_RANGE = 2;
	private static final int SPELL_POTENCY = 2;
	private static final float DAMAGE_PER_POTENCY = 0.5F;
	
	private static final EntityDataAccessor<InkColor> COLOR = SynchedEntityData.defineId(InkProjectileEntity.class, SpectrumTrackedDataHandlerRegistry.INK_COLOR);
	
	public InkProjectileEntity(EntityType<InkProjectileEntity> type, Level world) {
		super(type, world);
	}
	
	public InkProjectileEntity(double x, double y, double z, Level world) {
		this(SpectrumEntityTypes.INK_PROJECTILE, world);
		this.moveTo(x, y, z, this.getYRot(), this.getXRot());
		this.reapplyPosition();
	}
	
	public InkProjectileEntity(Level world, LivingEntity owner) {
		this(owner.getX(), owner.getEyeY() - 0.1, owner.getZ(), world);
		this.setOwner(owner);
		this.setRot(owner.getYRot(), owner.getXRot());
	}
	
	public static void shoot(Level world, LivingEntity entity, InkColor color) {
		InkProjectileEntity projectile = new InkProjectileEntity(world, entity);
		projectile.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 2.0F, 1.0F);
		projectile.setColor(color);
		world.addFreshEntity(projectile);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(COLOR, InkColors.RED);
	}
	
	@Override
	public InkColor getInkColor() {
		return this.entityData.get(COLOR);
	}
	
	public void setColor(InkColor inkColor) {
		this.entityData.set(COLOR, inkColor);
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		
		ResourceLocation inkColorId = SpectrumRegistries.INK_COLOR.getKey(this.getInkColor());
		if (inkColorId != null) {
			nbt.putString("ink_color", inkColorId.toString());
		}
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		
		if (nbt.contains("ink_color", Tag.TAG_STRING)) {
			ResourceLocation inkColorId = ResourceLocation.parse(nbt.getString("ink_color"));
			InkColor inkColor = SpectrumRegistries.INK_COLOR.get(inkColorId);
			if (inkColor != null) {
				this.setColor(inkColor);
			}
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		this.spawnParticles(1);
	}

	private void spawnParticles(int amount) {
		InkColor inkColor = this.getInkColor();
		if (amount > 0) {
			for (int j = 0; j < amount; ++j) {
				this.level().addParticle(ColoredCraftingParticleEffect.of(inkColor.getColorInt()), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0, 0, 0);
			}
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		
		Entity target = entityHitResult.getEntity();
		Entity owner = this.getOwner();
		
		if (EntityColorProcessorRegistry.colorEntity(target, getInkColor().getDyeColor(), owner instanceof Player player ? player : null)) {
			target.level().playSound(null, target, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
		}
		
		float velocity = (float) this.getDeltaMovement().length();
		int damage = Mth.ceil(Mth.clamp((double) velocity * DAMAGE_PER_POTENCY * SPELL_POTENCY, 0.0D, 2.147483647E9D));
		
		DamageSource damageSource;
		if (owner == null) {
			damageSource = SpectrumDamageTypes.inkProjectile(this, this);
		} else {
			damageSource = SpectrumDamageTypes.inkProjectile(this, owner);
			if (owner instanceof LivingEntity livingOwner) {
				livingOwner.setLastHurtMob(target);
			}
		}
		
		if (target.hurt(damageSource, (float) damage)) {
			if (target instanceof LivingEntity livingTarget) {
				
				if (this.level() instanceof ServerLevel serverWorld) {
					EnchantmentHelper.doPostAttackEffectsWithItemSource(serverWorld, target, damageSource, null);
				}
				
				this.onHit(livingTarget);
				
				if (target != owner && target instanceof Player && owner instanceof ServerPlayer ownerPlayer && !this.isSilent()) {
					ownerPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
				}
				
				if (owner instanceof ServerPlayer ownerPlayer && !target.isAlive()) {
					SpectrumAdvancementCriteria.KILLED_BY_INK_PROJECTILE.trigger(ownerPlayer, List.of(target));
				}
			}
			
			this.playSound(this.getHitSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
			this.discard();
		} else {
			this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
			this.setYRot(this.getYRot() + 180.0F);
			this.yRotO += 180.0F;
			if (!this.level().isClientSide() && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
				this.discard();
			}
		}
	}
	
	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		
		Vec3 vec3d = blockHitResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
		this.setDeltaMovement(vec3d);
		Vec3 vec3d2 = vec3d.normalize().scale(0.05);
		this.setPosRaw(this.getX() - vec3d2.x, this.getY() - vec3d2.y, this.getZ() - vec3d2.z);
		this.playSound(this.getHitSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
		
		InkColor inkColor = this.getInkColor();
		for (BlockPos blockPos : BlockPos.withinManhattan(blockHitResult.getBlockPos(), COLOR_SPLAT_RANGE, COLOR_SPLAT_RANGE, COLOR_SPLAT_RANGE)) {
			Optional<DyeColor> dyeColor = inkColor.getDyeColor();
			if (this.level().getBlockState(blockPos).getBlock() instanceof ColorableBlock colorableBlock) {
				if (!GenericClaimModsCompat.canModify(this.level(), blockPos, this.getOwner())) {
					continue;
				}
				colorableBlock.color(this.level(), blockPos, dyeColor, getOwner());
				continue;
			}
			if (dyeColor.isPresent()) {
				BlockState coloredBlockState = BlockVariantHelper.getCursedBlockColorVariant(this.level(), blockPos, dyeColor.get());
				if (!coloredBlockState.isAir()) {
					this.level().setBlockAndUpdate(blockPos, coloredBlockState);
				}
			}
		}
		
		affectEntitiesInRange(this.getOwner());
		
		this.discard();
	}

	protected void onHit(LivingEntity target) {
		// TODO: this is a dummy effect. Add unique effects for each ink color
		Vec3 vec3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double) 3 * 0.6D);
		if (vec3d.lengthSqr() > 0.0D) {
			target.push(vec3d.x, 0.1D, vec3d.z);
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
		
		this.discard();
	}
	
	public void affectEntitiesInRange(Entity attacker) {
		this.level().gameEvent(this, GameEvent.PROJECTILE_LAND, BlockPos.containing(this.position().x, this.position().y, this.position().z));
		
		double posX = this.position().x;
		double posY = this.position().y;
		double posZ = this.position().z;
		
		float q = SPELL_POTENCY * 2.0F;
		double k = Mth.floor(posX - (double) q - 1.0D);
		double l = Mth.floor(posX + (double) q + 1.0D);
		int r = Mth.floor(posY - (double) q - 1.0D);
		int s = Mth.floor(posY + (double) q + 1.0D);
		int t = Mth.floor(posZ - (double) q - 1.0D);
		int u = Mth.floor(posZ + (double) q + 1.0D);
		List<Entity> list = this.level().getEntities(this, new AABB(k, r, t, l, s, u));
		Vec3 vec3d = new Vec3(posX, posY, posZ);
		
		Entity owner = this.getOwner();
		for (Entity entity : list) {
			if (!GenericClaimModsCompat.canInteract(this.level(), entity, attacker)) {
				continue;
			}
			
			if (EntityColorProcessorRegistry.colorEntity(entity, getInkColor().getDyeColor(), owner instanceof Player player ? player : null)) {
				entity.level().playSound(null, entity, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
			
			if (!entity.ignoreExplosion(null)) {
				double w = Math.sqrt(entity.distanceToSqr(vec3d)) / (double) q;
				if (w <= 1.0D) {
					double x = entity.getX() - posX;
					double y = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - posY;
					double z = entity.getZ() - posZ;
					double aa = Math.sqrt(x * x + y * y + z * z);
					if (aa != 0.0D) {
						x /= aa;
						y /= aa;
						z /= aa;
						double ab = Explosion.getSeenPercent(vec3d, entity);
						double velocity = (1.0D - w) * ab;
						
						if (entity instanceof LivingEntity livingEntity) {
							int i = SpectrumEnchantmentHelper.getEquipmentLevel(level().registryAccess(), Enchantments.BLAST_PROTECTION, livingEntity);
							if (i > 0) {
								velocity *= Mth.clamp(1.0 - (double) i * 0.15, 0.0, 1.0);
							}
						}
						
						entity.setDeltaMovement(entity.getDeltaMovement().add(x * velocity, y * velocity, z * velocity));
					}
				}
			}
		}
	}
	
	@Override
	public void spawnImpactParticles() {
		InkColor inkColor = getInkColor();
		Level world = level();
		Vec3 targetPos = position();
		Vec3 velocity = getDeltaMovement();
		
		world.addParticle(ColoredExplosionParticleEffect.of(inkColor.getColorInt()), targetPos.x, targetPos.y, targetPos.z, 0, 0, 0);
		for (int i = 0; i < 10; i++) {
			world.addParticle(ColoredCraftingParticleEffect.of(inkColor.getColorInt()), targetPos.x, targetPos.y, targetPos.z, -velocity.x * 3, -velocity.y * 3, -velocity.z * 3);
		}
	}
	
}
