package de.dafuqs.spectrum.blocks.pedestal;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PedestalUpgradeBlock extends BlockWithEntity {
	
	// TODO: maybe even make it settable via NBT?
	// Like: The further the player progresses,
	// the higher are the chances for good mods?
	private final Upgradeable.UpgradeType upgradeType;
	private final double upgradeMod;
	
	protected static final VoxelShape SHAPE_UP = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);

	public PedestalUpgradeBlock(Settings settings, Upgradeable.UpgradeType upgradeType, double upgradeMod) {
		super(settings);
		this.upgradeType = upgradeType;
		this.upgradeMod = upgradeMod;
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE_UP;
	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		updateConnectedPedestal(world, pos);
		super.onBlockAdded(state, world, pos, oldState, notify);
	}


	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		updateConnectedPedestal(world, pos);
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	/**
	 * When placed or removed the upgrade block searches for a valid pedestal
	 * and triggers it to update its upgrades
	 */
	private void updateConnectedPedestal(@NotNull World world, @NotNull BlockPos pos) {
		if(world.getBlockState(pos.add(3, -2, 3)).getBlock() instanceof PedestalBlock) {
			PedestalBlock.updateUpgrades(world, pos.add(3, -2, 3));
		} else if(world.getBlockState(pos.add(3, -2, -3)).getBlock() instanceof PedestalBlock) {
			PedestalBlock.updateUpgrades(world, pos.add(3, -2, -3));
		} else if(world.getBlockState(pos.add(-3, -2, 3)).getBlock() instanceof PedestalBlock) {
			PedestalBlock.updateUpgrades(world, pos.add(-3, -2, 3));
		} else if(world.getBlockState(pos.add(-3, -2, -3)).getBlock() instanceof PedestalBlock) {
			PedestalBlock.updateUpgrades(world, pos.add(-3, -2, -3));
		}
	}
	
	public Upgradeable.UpgradeType getUpgradeType() {
		return this.upgradeType;
	}
	
	public double getUpgradeMod() {
		return this.upgradeMod;
	}
	
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PedestalUpgradeBlockEntity(pos, state);
	}
	
}
