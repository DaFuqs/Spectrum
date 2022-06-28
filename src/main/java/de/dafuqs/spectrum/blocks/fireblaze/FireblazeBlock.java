package de.dafuqs.spectrum.blocks.fireblaze;

import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Random;

public class FireblazeBlock extends BlockWithEntity {
	
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	
	public FireblazeBlock(Settings settings) {
		super(settings);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FireblazeBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return null;
		} else {
			return checkType(type, SpectrumBlockEntityRegistry.FIREBLAZE, FireblazeBlockEntity::serverTick);
		}
	}
	
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof FireblazeBlockEntity fireblazeBlockEntity) {
			player.openHandledScreen(fireblazeBlockEntity);
		}
	}
	
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			this.openScreen(world, pos, player);
			return ActionResult.CONSUME;
		}
	}
	
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}
	
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof FireblazeBlockEntity fireblazeBlockEntity) {
				fireblazeBlockEntity.setCustomName(itemStack.getName());
			}
		}
		
	}
	
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof FireblazeBlockEntity fireblazeBlockEntity) {
				if (world instanceof ServerWorld) {
					ItemScatterer.spawn(world, pos, fireblazeBlockEntity);
				}
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
	
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		double d = (double)pos.getX() + 0.5D;
		double e = pos.getY();
		double f = (double)pos.getZ() + 0.5D;
		if (random.nextDouble() < 0.1D) {
			world.playSound(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
		}
		
		Direction direction = state.get(FACING);
		Direction.Axis axis = direction.getAxis();
		double g = 0.52D;
		double h = random.nextDouble() * 0.6D - 0.3D;
		double i = axis == Direction.Axis.X ? (double)direction.getOffsetX() * 0.52D : h;
		double j = random.nextDouble() * 6.0D / 16.0D;
		double k = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * 0.52D : h;
		world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
	}
	
	public static boolean verifyStructure(World world, @NotNull BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity) {
		IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.FIREBLAZE_IDENTIFIER);
		
		boolean valid = multiblock.validate(world, blockPos.down(1), BlockRotation.NONE);
		
		if (valid) {
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblock);
			}
		} else {
			if (world.isClient) {
				PatchouliAPI.get().showMultiblock(multiblock, new TranslatableText("multiblock.spectrum.spirit_instiller.structure"), blockPos.down(2), BlockRotation.NONE);
			}
		}
		
		return valid;
	}
	
}
