package de.dafuqs.spectrum.blocks.mob_blocks;

import de.dafuqs.spectrum.networking.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class MobBlock extends Block {
	
	public static final BooleanProperty COOLDOWN = BooleanProperty.of("cooldown");
	public final ParticleEffect particleEffect;
	
	public MobBlock(Settings settings, ParticleEffect particleEffect) {
		super(settings);
		this.particleEffect = particleEffect;
		setDefaultState(getStateManager().getDefaultState().with(COOLDOWN, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(COOLDOWN);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.mob_block.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			if (!hasCooldown(state) && trigger((ServerWorld) world, pos, state, player, hit.getSide())) {
				playTriggerParticles((ServerWorld) world, pos);
				playTriggerSound(world, pos);
				triggerCooldown(world, pos);
			}
			return ActionResult.CONSUME;
		} else {
			return ActionResult.SUCCESS;
		}
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		world.setBlockState(pos, world.getBlockState(pos).with(COOLDOWN, false));
	}
	
	@Override
	public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
		super.onSteppedOn(world, pos, state, entity);
		if (!world.isClient && !hasCooldown(state)) {
			if (trigger((ServerWorld) world, pos, state, entity, Direction.UP)) {
				playTriggerParticles((ServerWorld) world, pos);
				playTriggerSound(world, pos);
				triggerCooldown(world, pos);
			}
		}
	}
	
	@Override
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		if (!world.isClient) {
			BlockPos hitPos = hit.getBlockPos();
			if (!hasCooldown(state) && trigger((ServerWorld) world, hitPos, state, projectile.getOwner(), hit.getSide())) {
				playTriggerParticles((ServerWorld) world, hit.getBlockPos());
				playTriggerSound(world, hitPos);
				triggerCooldown(world, hitPos);
			}
		}
	}
	
	public abstract boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side);
	
	public void playTriggerParticles(ServerWorld world, BlockPos blockPos) {
		SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, new Vec3d(blockPos.getX() + 0.5, blockPos.getY() + 0.2, blockPos.getZ() + 0.5), particleEffect, 10, new Vec3d(0.5, 0.5, 0.5), new Vec3d(0.2, 0.08, 0.2));
	}
	
	public void playTriggerSound(World world, BlockPos blockPos) {
		world.playSound(null, blockPos, this.soundGroup.getPlaceSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
	}
	
	public boolean hasCooldown(BlockState state) {
		return state.get(COOLDOWN);
	}
	
	public void triggerCooldown(World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).with(COOLDOWN, true));
		world.scheduleBlockTick(pos, this, getCooldownTicks());
	}
	
	public int getCooldownTicks() {
		return 40;
	}
	
	public Position getOutputLocation(BlockPointer pointer, Direction direction) {
		double d = pointer.getX() + 0.7D * (double) direction.getOffsetX();
		double e = pointer.getY() + 0.7D * (double) direction.getOffsetY();
		double f = pointer.getZ() + 0.7D * (double) direction.getOffsetZ();
		return new PositionImpl(d, e, f);
	}
	
}
