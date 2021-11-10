package de.dafuqs.spectrum.blocks.pedestal;

import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlock;
import de.dafuqs.spectrum.blocks.fusion_shrine.FusionShrineBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PedestalUpgradeBlock extends BlockWithEntity {
	
	// Positions to check on place / destroy to upgrade those blocks upgrade counts
	private List<Vec3i> possibleUpgradeBlockOffsets = new ArrayList<>() {{
		// Pedestal
		add(new Vec3i(3, 2, 3));
		add(new Vec3i(-3, 2, 3));
		add(new Vec3i(3, 2, -3));
		add(new Vec3i(-3, 2, -3));
		
		// Fusion Shrine
		add(new Vec3i(2, 0, 2));
		add(new Vec3i(-2, 0, 2));
		add(new Vec3i(2, 0, -2));
		add(new Vec3i(-2, 0, -2));
	}};
	
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
		updateConnectedUpgradeBlock(world, pos);
		super.onBlockAdded(state, world, pos, oldState, notify);
	}


	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		updateConnectedUpgradeBlock(world, pos);
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	/**
	 * When placed or removed the upgrade block searches for a valid pedestal
	 * and triggers it to update its upgrades
	 */
	private void updateConnectedUpgradeBlock(@NotNull World world, @NotNull BlockPos pos) {
		for(Vec3i possibleUpgradeBlockOffset : possibleUpgradeBlockOffsets) {
			BlockPos currentPos = pos.add(possibleUpgradeBlockOffset);
			Block block = world.getBlockState(currentPos).getBlock();
			if(block instanceof PedestalBlock) {
				BlockEntity blockEntity = world.getBlockEntity(currentPos);
				if(blockEntity instanceof PedestalBlockEntity pedestalBlockEntity) {
					pedestalBlockEntity.updateUpgrades();
				}
				break;
			} else if(block instanceof FusionShrineBlock) {
				BlockEntity blockEntity = world.getBlockEntity(currentPos);
				if(blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
					fusionShrineBlockEntity.updateUpgrades();
				}
				break;
			}
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
