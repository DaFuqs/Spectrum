package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

public class BlockBreakerBlock extends RedstoneInteractionBlock implements EntityBlock {
	
	
	public static final MapCodec<BlockBreakerBlock> CODEC = simpleCodec(BlockBreakerBlock::new);

	private static ItemStack BREAK_STACK;
	
	public BlockBreakerBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends BlockBreakerBlock> codec() {
		return CODEC;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlockBreakerBlockEntity(pos, state);
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (placer instanceof ServerPlayer serverPlayerEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof BlockBreakerBlockEntity blockBreakerBlockEntity) {
				blockBreakerBlockEntity.setOwner(serverPlayerEntity);
			}
		}
	}
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		var isTriggered = world.hasNeighborSignal(pos) || world.hasNeighborSignal(pos.above());
		boolean wasTriggered = state.getValue(TRIGGERED);
		
		if (isTriggered && !wasTriggered) {
			if (!world.isClientSide) {
				this.destroy((ServerLevel) world, pos, state.getValue(ORIENTATION).front());
			}
			world.setBlock(pos, state.setValue(TRIGGERED, true), Block.UPDATE_INVISIBLE);
		} else if (!isTriggered && wasTriggered) {
			world.setBlock(pos, state.setValue(TRIGGERED, false), Block.UPDATE_INVISIBLE);
		}
	}
	
	protected void destroy(ServerLevel world, BlockPos breakerPos, Direction direction) {
		BlockPos breakingPos = breakerPos.relative(direction);
		BlockState blockState = world.getBlockState(breakingPos);
		
		if (blockState.isAir() || blockState.getBlock() instanceof BaseFireBlock) {
			return;
		}
		
		float hardness = blockState.getDestroySpeed(world, breakingPos);
		if (hardness < 0 || hardness > 50) {
			world.playSound(null, breakerPos, SpectrumSoundEvents.REDSTONE_MECHANISM_BREAK_BLOCK, SoundSource.BLOCKS, 0.15f, (2.0f + world.random.nextFloat()));
			return;
		}
		
		BlockEntity blockEntity = world.getBlockEntity(breakerPos);
		if (!(blockEntity instanceof BlockBreakerBlockEntity blockBreakerBlockEntity)) {
			return;
		}
		@Nullable Player owner = blockBreakerBlockEntity.getFakeOwner(world);
		if (!GenericClaimModsCompat.canBreak(world, breakingPos, owner)) {
			return;
		}
		
		this.breakBlock(world, breakingPos, owner);
		
		Vec3 centerPos = Vec3.atCenterOf(breakingPos);
		world.sendParticles(ParticleTypes.EXPLOSION, centerPos.x(), centerPos.y(), centerPos.z(), 1, 0.0, 0.0, 0.0, 1.0);
	}
	
	public void breakBlock(ServerLevel world, BlockPos pos, @Nullable Player breaker) {
		BlockState blockState = world.getBlockState(pos);
		FluidState fluidState = world.getFluidState(pos);
		
		world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(blockState));
		world.playSound(null, pos, blockState.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.2f, (1.0f + world.random.nextFloat()) * 2f);
		
		BlockEntity blockEntity = blockState.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		if (BREAK_STACK == null) { // we initialize the item here instead of it being final because of load order shenanigans
			BREAK_STACK = new ItemStack(SpectrumItems.MALACHITE_WORKSTAFF);
		}
		
		if (world.setBlock(pos, fluidState.createLegacyBlock(), Block.UPDATE_ALL, 512)) {
			Block block = blockState.getBlock();
			if (breaker == null) {
				block.destroy(world, pos, blockState);
			} else {
				block.playerDestroy(world, breaker, pos, blockState, blockEntity, BREAK_STACK);
			}
			world.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(breaker, blockState));
		}
	}
	
}
