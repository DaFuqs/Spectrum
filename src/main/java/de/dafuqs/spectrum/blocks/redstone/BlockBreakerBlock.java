package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

public class BlockBreakerBlock extends RedstoneInteractionBlock implements BlockEntityProvider {
	
	private static ItemStack BREAK_STACK;
	
	public BlockBreakerBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BlockBreakerBlockEntity(pos, state);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (placer instanceof ServerPlayerEntity serverPlayerEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BlockBreakerBlockEntity blockBreakerBlockEntity) {
				blockBreakerBlockEntity.setOwner(serverPlayerEntity);
			}
		}
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		var isTriggered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
		boolean wasTriggered = state.get(TRIGGERED);
		
		if (isTriggered && !wasTriggered) {
			if (!world.isClient) {
				this.destroy(world, pos, state.get(ORIENTATION).getFacing());
			}
			world.setBlockState(pos, state.with(TRIGGERED, true), Block.NO_REDRAW);
		} else if (!isTriggered && wasTriggered) {
			world.setBlockState(pos, state.with(TRIGGERED, false), Block.NO_REDRAW);
		}
	}
	
	protected void destroy(World world, BlockPos breakerPos, Direction direction) {
		BlockPos breakingPos = breakerPos.offset(direction);
		BlockState blockState = world.getBlockState(breakingPos);
		
		if (blockState.isAir() || blockState.getBlock() instanceof AbstractFireBlock) {
			return;
		}
		
		float hardness = blockState.getHardness(world, breakingPos);
		if (hardness < 0 || hardness > 50) {
			world.playSound(null, breakerPos, SpectrumSoundEvents.REDSTONE_MECHANISM_BREAK_BLOCK, SoundCategory.BLOCKS, 0.15f, (2.0f + world.random.nextFloat()));
			return;
		}
		
		BlockEntity blockEntity = world.getBlockEntity(breakerPos);
		if (!(blockEntity instanceof BlockBreakerBlockEntity blockBreakerBlockEntity)) {
			return;
		}
		PlayerEntity owner = blockBreakerBlockEntity.getOwnerIfOnline();
		
		if (!GenericClaimModsCompat.canBreak(world, breakingPos, owner)) {
			return;
		}
		
		this.breakBlock(world, breakingPos, owner);
		
		Vec3d centerPos = Vec3d.ofCenter(breakingPos);
		((ServerWorld) world).spawnParticles(ParticleTypes.EXPLOSION, centerPos.getX(), centerPos.getY(), centerPos.getZ(), 1, 0.0, 0.0, 0.0, 1.0);
	}
	
	public void breakBlock(World world, BlockPos pos, PlayerEntity breaker) {
		BlockState blockState = world.getBlockState(pos);
		FluidState fluidState = world.getFluidState(pos);
		
		world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(blockState));
		world.playSound(null, pos, blockState.getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 0.2f, (1.0f + world.random.nextFloat()) * 2f);
		
		BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		if (BREAK_STACK == null) { // we initialize the item here instead of it being final because of load order shenanigans
			BREAK_STACK = new ItemStack(SpectrumItems.MALACHITE_WORKSTAFF);
		}
		Block.dropStacks(blockState, world, pos, blockEntity, breaker, BREAK_STACK);
		
		if (world.setBlockState(pos, fluidState.getBlockState(), Block.NOTIFY_ALL, 512)) {
			world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(breaker, blockState));
		}
	}
	
}
