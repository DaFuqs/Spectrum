package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.world.*;

import java.util.function.*;

public class MiningProjectileEntity extends MagicProjectileEntity {
	
	private static final int MINING_RANGE = 1;
	private ItemStack toolStack = ItemStack.EMPTY;

	public MiningProjectileEntity(EntityType<MiningProjectileEntity> type, World world) {
		super(type, world);
	}

	public MiningProjectileEntity(double x, double y, double z, World world) {
		this(SpectrumEntityTypes.MINING_PROJECTILE, world);
		this.refreshPositionAndAngles(x, y, z, this.getYaw(), this.getPitch());
		this.refreshPosition();
	}

	public MiningProjectileEntity(World world, LivingEntity owner) {
		this(owner.getX(), owner.getEyeY() - 0.10000000149011612D, owner.getZ(), world);
		this.setOwner(owner);
		this.setRotation(owner.getYaw(), owner.getPitch());
	}
	
	public static void shoot(World world, LivingEntity entity, ItemStack stack) {
		MiningProjectileEntity projectile = new MiningProjectileEntity(world, entity);
		projectile.setVelocity(entity, entity.getPitch(), entity.getYaw(), 0.0F, 2.0F, 1.0F);
		projectile.toolStack = stack.copy();
		world.spawnEntity(projectile);
	}

	@Override
	protected void initDataTracker() {

	}

	@Override
	public void tick() {
		super.tick();
		this.spawnParticles(1);
	}

	private void spawnParticles(int amount) {
		for (int j = 0; j < amount; ++j) {
			this.world.addParticle(SpectrumParticleTypes.WHITE_CRAFTING, this.getParticleX(0.5D), this.getRandomBodyY(), this.getParticleZ(0.5D), 0, 0, 0);
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		this.discard();
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		this.playSound(this.getHitSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));

		Entity entity = getOwner();
		if (entity instanceof PlayerEntity player) {
			Predicate<BlockState> minablePredicate = state -> {
				int miningLevel = this.toolStack.getItem() instanceof ToolItem toolItem ? toolItem.getMaterial().getMiningLevel() : 1;
				int efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, this.toolStack);
				return state.getBlock().getHardness() <= miningLevel + efficiency;
			};
			AoEHelper.breakBlocksAround(player, this.toolStack, blockHitResult.getBlockPos(), MINING_RANGE, minablePredicate);
		}

		this.discard();
	}

	@Override
	public DyeColor getDyeColor() {
		return DyeColor.WHITE;
	}

}
