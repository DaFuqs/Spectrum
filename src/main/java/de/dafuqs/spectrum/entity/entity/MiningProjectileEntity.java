package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.magic.*;
import net.minecraft.network.syncher.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

import java.util.function.*;

public class MiningProjectileEntity extends MagicProjectileEntity {
	
	private static final int MINING_RANGE = 1;
	private ItemStack toolStack = ItemStack.EMPTY;
	
	public MiningProjectileEntity(EntityType<MiningProjectileEntity> type, Level world) {
		super(type, world);
	}
	
	public MiningProjectileEntity(double x, double y, double z, Level world) {
		this(SpectrumEntityTypes.MINING_PROJECTILE.get(), world);
		this.moveTo(x, y, z, this.getYRot(), this.getXRot());
		this.reapplyPosition();
	}
	
	public MiningProjectileEntity(Level world, LivingEntity owner) {
		this(owner.getX(), owner.getEyeY() - 0.1, owner.getZ(), world);
		this.setOwner(owner);
		this.setRot(owner.getYRot(), owner.getXRot());
	}
	
	public static void shoot(Level world, LivingEntity entity, ItemStack stack) {
		MiningProjectileEntity projectile = new MiningProjectileEntity(world, entity);
		projectile.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 2.0F, 1.0F);
		projectile.toolStack = stack.copy();
		world.addFreshEntity(projectile);
	}
	
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	
	}
	
	@Override
	public void tick() {
		super.tick();
		this.spawnParticles(1);
	}
	
	private void spawnParticles(int amount) {
		for (int j = 0; j < amount; ++j) {
			this.level().addParticle(ColoredCraftingParticleEffect.WHITE, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0, 0, 0);
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		MoonstoneStrike.create(this.level(), this, null, this.getX(), this.getY(), this.getZ(), 1);
		this.discard();
	}
	
	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		
		MoonstoneStrike.create(this.level(), this, null, this.getX(), this.getY(), this.getZ(), 1);
		
		Entity entity = getOwner();
		if (entity instanceof Player player) {
			Predicate<BlockState> minablePredicate = state -> {
				float hardness = state.getDestroySpeed(level(), blockHitResult.getBlockPos());
				if (hardness < 0) {
					return false;
				}
				boolean suitable = this.toolStack.isCorrectToolForDrops(state);
				int efficiencyLevel = SpectrumEnchantmentHelper.getLevel(level().registryAccess(), Enchantments.EFFICIENCY, this.toolStack);
				return hardness <= 6 + (suitable ? 4 + efficiencyLevel : 0);
			};
			AoEHelper.breakBlocksAround(player, this.toolStack, blockHitResult.getBlockPos(), MINING_RANGE, minablePredicate);
		}
		
		this.discard();
	}
	
	@Override
	public void spawnImpactParticles() {
	}
	
	@Override
	public InkColor getInkColor() {
		return InkColors.WHITE;
	}
	
}
