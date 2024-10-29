package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.blocks.idols.FirestarterIdolBlock;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import net.minecraft.world.explosion.*;

import java.util.*;

public class InkProjectileEntity extends MagicProjectileEntity {

	private static final int COLOR_SPLAT_RANGE = 2;
	private static final int SPELL_POTENCY = 2;
	private static final float DAMAGE_PER_POTENCY = 0.5F;

	private static final TrackedData<Integer> COLOR = DataTracker.registerData(InkProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public InkProjectileEntity(EntityType<InkProjectileEntity> type, World world) {
		super(type, world);
	}

	public InkProjectileEntity(double x, double y, double z, World world) {
		this(SpectrumEntityTypes.INK_PROJECTILE, world);
		this.refreshPositionAndAngles(x, y, z, this.getYaw(), this.getPitch());
		this.refreshPosition();
	}

	public InkProjectileEntity(World world, LivingEntity owner) {
		this(owner.getX(), owner.getEyeY() - 0.1, owner.getZ(), world);
		this.setOwner(owner);
		this.setRotation(owner.getYaw(), owner.getPitch());
	}

	public static void shoot(World world, LivingEntity entity, InkColor color) {
		InkProjectileEntity projectile = new InkProjectileEntity(world, entity);
		projectile.setVelocity(entity, entity.getPitch(), entity.getYaw(), 0.0F, 2.0F, 1.0F);
		projectile.setColor(color);
		world.spawnEntity(projectile);
	}

	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(COLOR, -1);
	}

	public int getColor() {
		return this.dataTracker.get(COLOR);
	}
	
	@Override
	public InkColor getInkColor() {
		return InkColor.ofDyeColor(DyeColor.byId(this.dataTracker.get(COLOR)));
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
	}

	private void spawnParticles(int amount) {
		int colorOrdinal = this.getColor();
		if (colorOrdinal != -1 && amount > 0) {
			DyeColor dyeColor = DyeColor.byId(colorOrdinal);
			for (int j = 0; j < amount; ++j) {
				this.getWorld().addParticle(SpectrumParticleTypes.getCraftingParticle(dyeColor), this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0, 0, 0);
			}
		}
	}
	
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		
		Entity entity = entityHitResult.getEntity();
		
		if (EntityColorProcessorRegistry.colorEntity(entity, getInkColor().getDyeColor())) {
			entity.getWorld().playSoundFromEntity(null, entity, SoundEvents.ITEM_DYE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		
		float velocity = (float) this.getVelocity().length();
		int damage = MathHelper.ceil(MathHelper.clamp((double) velocity * DAMAGE_PER_POTENCY * SPELL_POTENCY, 0.0D, 2.147483647E9D));
		
		Entity entity2 = this.getOwner();
		DamageSource damageSource;
		if (entity2 == null) {
			damageSource = SpectrumDamageTypes.inkProjectile(this, this);
		} else {
			damageSource = SpectrumDamageTypes.inkProjectile(this, entity2);
			if (entity2 instanceof LivingEntity) {
				((LivingEntity) entity2).onAttacking(entity);
			}
		}
		
		if (entity.damage(damageSource, (float) damage)) {
			if (entity instanceof LivingEntity livingEntity) {
				
				if (!this.getWorld().isClient() && entity2 instanceof LivingEntity) {
					EnchantmentHelper.onUserDamaged(livingEntity, entity2);
					EnchantmentHelper.onTargetDamaged((LivingEntity) entity2, livingEntity);
				}
				
				this.onHit(livingEntity);
				
				if (livingEntity != entity2 && livingEntity instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
					((ServerPlayerEntity) entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0F));
				}
				
				if (!this.getWorld().isClient() && entity2 instanceof ServerPlayerEntity serverPlayerEntity) {
					if (!entity.isAlive()) {
						SpectrumAdvancementCriteria.KILLED_BY_INK_PROJECTILE.trigger(serverPlayerEntity, List.of(entity));
					}
				}
			}
			
			this.playSound(this.getHitSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
			this.discard();
		} else {
			this.setVelocity(this.getVelocity().multiply(-0.1D));
			this.setYaw(this.getYaw() + 180.0F);
			this.prevYaw += 180.0F;
			if (!this.getWorld().isClient() && this.getVelocity().lengthSquared() < 1.0E-7D) {
				this.discard();
			}
		}
	}
	
	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		
		Vec3d vec3d = blockHitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
		this.setVelocity(vec3d);
		Vec3d vec3d2 = vec3d.normalize().multiply(0.05);
		this.setPos(this.getX() - vec3d2.x, this.getY() - vec3d2.y, this.getZ() - vec3d2.z);
		this.playSound(this.getHitSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
		
		int colorOrdinal = this.getColor();
		if (colorOrdinal != -1) {
			DyeColor dyeColor = DyeColor.byId(colorOrdinal);
			
			for (BlockPos blockPos : BlockPos.iterateOutwards(blockHitResult.getBlockPos(), COLOR_SPLAT_RANGE, COLOR_SPLAT_RANGE, COLOR_SPLAT_RANGE)) {
				if (this.getWorld().getBlockState(blockPos).getBlock() instanceof ColorableBlock colorableBlock) {
					if (!GenericClaimModsCompat.canModify(this.getWorld(), blockPos, this.getOwner())) {
						continue;
					}
					colorableBlock.color(this.getWorld(), blockPos, dyeColor);
					continue;
				}
				BlockState coloredBlockState = BlockVariantHelper.getCursedBlockColorVariant(this.getWorld(), blockPos, dyeColor);
				if (!coloredBlockState.isAir()) {
					this.getWorld().setBlockState(blockPos, coloredBlockState);
				}
				if(this.getInkColor() == InkColors.ORANGE && this.getWorld().getBlockState(blockPos).getBlock() == Blocks.SNOW)
				{
					this.getWorld().setBlockState(blockPos, Blocks.AIR.getDefaultState());
				}
			}
			if(this.getInkColor() == InkColors.ORANGE)
			{
				FirestarterIdolBlock.causeFire((ServerWorld) this.getWorld(), blockHitResult.getBlockPos(), blockHitResult.getSide());
			}
			affectEntitiesInRange(this.getOwner());
		}
		
		this.discard();
	}

	protected void onHit(LivingEntity target) {
		int colorOrdinal = this.getColor();
		if (colorOrdinal != -1) {
			//InkColor.all().get(colorOrdinal);
			
			
			Entity entity = target; //this.getEffectCause();

			if(this.getInkColor() == InkColors.ORANGE)
			{
				entity.setOnFireFor(2);
			}
			else
			{
				// TODO: this is a dummy effect
				Vec3d vec3d = this.getVelocity().multiply(1.0D, 0.0D, 1.0D).normalize().multiply((double) 3 * 0.6D);
				if (vec3d.lengthSquared() > 0.0D) {
					entity.addVelocity(vec3d.x, 0.1D, vec3d.z);
				}
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
		this.getWorld().emitGameEvent(this, GameEvent.PROJECTILE_LAND, BlockPos.ofFloored(this.getPos().x, this.getPos().y, this.getPos().z));
		
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
		List<Entity> list = this.getWorld().getOtherEntities(this, new Box(k, r, t, l, s, u));
		Vec3d vec3d = new Vec3d(posX, posY, posZ);
		
		for (Entity entity : list) {
			if (!GenericClaimModsCompat.canInteract(this.getWorld(), entity, attacker)) {
				continue;
			}
			
			if (EntityColorProcessorRegistry.colorEntity(entity, getInkColor().getDyeColor())) {
				entity.getWorld().playSoundFromEntity(null, entity, SoundEvents.ITEM_DYE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
			
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
						if (entity instanceof LivingEntity livingEntity) {
							ad = ProtectionEnchantment.transformExplosionKnockback(livingEntity, ac);
						}
						
						entity.setVelocity(entity.getVelocity().add(x * ad, y * ad, z * ad));
					}
				}
			}
		}
	}
	
	@Override
	public void spawnImpactParticles() {
		DyeColor dyeColor = getInkColor().getDyeColor();
		World world = getWorld();
		Vec3d targetPos = getPos();
		Vec3d velocity = getVelocity();
		
		world.addParticle(SpectrumParticleTypes.getExplosionParticle(dyeColor), targetPos.x, targetPos.y, targetPos.z, 0, 0, 0);
		for (int i = 0; i < 10; i++) {
			world.addParticle(SpectrumParticleTypes.getCraftingParticle(dyeColor), targetPos.x, targetPos.y, targetPos.z, -velocity.x * 3, -velocity.y * 3, -velocity.z * 3);
		}
	}
	
}
