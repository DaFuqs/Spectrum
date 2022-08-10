package de.dafuqs.spectrum.blocks.cinderhearth;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
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
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Random;

public class CinderhearthBlock extends BlockWithEntity {
	
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	
	public CinderhearthBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.EAST));
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CinderhearthBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return null;
		} else {
			return checkType(type, SpectrumBlockEntities.CINDERHEARTH, CinderhearthBlockEntity::serverTick);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			verifyStructure(world, pos, null);
			return ActionResult.SUCCESS;
		} else {
			if (verifyStructure(world, pos, (ServerPlayerEntity) player)) {
				this.openScreen(world, pos, player);
			}
			return ActionResult.CONSUME;
		}
	}
	
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
			cinderhearthBlockEntity.setOwner(player);
			player.openHandledScreen(cinderhearthBlockEntity);
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
			if(placer instanceof PlayerEntity player) {
				cinderhearthBlockEntity.setOwner(player);
			}
			if (itemStack.hasCustomName()) {
				cinderhearthBlockEntity.setCustomName(itemStack.getName());
			}
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
				if (world instanceof ServerWorld) {
					ItemScatterer.spawn(world, pos, cinderhearthBlockEntity);
				}
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
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
		double i = axis == Direction.Axis.X ? (double)direction.getOffsetX() * g : h;
		double j = random.nextDouble() * 6.0D / 16.0D;
		double k = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * g : h;
		world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
	}
	
	public static boolean verifyStructure(World world, @NotNull BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity) {
		BlockRotation rotation = Support.rotationFromDirection(world.getBlockState(blockPos).get(FACING));
		
		IMultiblock multiblockWithLava = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.CINDERHEARTH_IDENTIFIER);
		IMultiblock multiblockWithoutLava = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.CINDERHEARTH_WITHOUT_LAVA_IDENTIFIER);
		if(world.isClient) {
			if (multiblockWithoutLava.validate(world, blockPos.down(3), rotation)) {
				return true;
			} else {
				PatchouliAPI.get().showMultiblock(multiblockWithLava, new TranslatableText("multiblock.spectrum.cinderhearth.structure"), blockPos.down(4), rotation);
				return false;
			}
		} else {
			if (multiblockWithLava.validate(world, blockPos.down(3), rotation)) {
				if (serverPlayerEntity != null) {
					SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblockWithLava);
				}
				return true;
			} else {
				if (multiblockWithoutLava.validate(world, blockPos.down(3), rotation)) {
					SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblockWithoutLava);
					return true;
				}
				return false;
			}
		}
	}
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			clearCurrentlyRenderedMultiBlock();
		}
	}
	
	public static void clearCurrentlyRenderedMultiBlock() {
		IMultiblock currentlyRenderedMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
		if (currentlyRenderedMultiBlock != null && currentlyRenderedMultiBlock.getID().equals(SpectrumMultiblocks.CINDERHEARTH_IDENTIFIER)) {
			PatchouliAPI.get().clearMultiblock();
		}
	}
	
}
