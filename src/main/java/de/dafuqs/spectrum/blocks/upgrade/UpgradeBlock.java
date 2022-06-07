package de.dafuqs.spectrum.blocks.upgrade;

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

public class UpgradeBlock extends BlockWithEntity {
	
	protected static final VoxelShape SHAPE_UP = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
	private static final List<Block> registeredUpgradeBlocks = new ArrayList<>();
	// Positions to check on place / destroy to upgrade those blocks upgrade counts
	private final List<Vec3i> possibleUpgradeBlockOffsets = new ArrayList<>() {{
		// Pedestal
		add(new Vec3i(3, -2, 3));
		add(new Vec3i(-3, -2, 3));
		add(new Vec3i(3, -2, -3));
		add(new Vec3i(-3, -2, -3));
		
		// Fusion Shrine
		add(new Vec3i(2, 0, 2));
		add(new Vec3i(-2, 0, 2));
		add(new Vec3i(2, 0, -2));
		add(new Vec3i(-2, 0, -2));
		
		// Enchanter
		add(new Vec3i(3, 0, 3));
		add(new Vec3i(-3, 0, 3));
		add(new Vec3i(3, 0, -3));
		add(new Vec3i(-3, 0, -3));
		
		// Spirit Instiller
		add(new Vec3i(4, -1, 4));
		add(new Vec3i(-4, -1, 4));
		add(new Vec3i(4, -1, -4));
		add(new Vec3i(-4, -1, -4));
	}};
	// Like: The further the player progresses,
	// the higher are the chances for good mods?
	private final Upgradeable.UpgradeType upgradeType;
	private final double upgradeMod;
	
	public UpgradeBlock(Settings settings, Upgradeable.UpgradeType upgradeType, double upgradeMod) {
		super(settings);
		this.upgradeType = upgradeType;
		this.upgradeMod = upgradeMod;
		
		registeredUpgradeBlocks.add(this);
	}
	
	public static List<Block> getRegisteredUpgradeBlocks() {
		return registeredUpgradeBlocks;
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE_UP;
	}
	
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		updateConnectedUpgradeBlock(world, pos);
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		updateConnectedUpgradeBlock(world, pos);
	}
	
	/**
	 * When placed or removed the upgrade block searches for a valid Upgradeable block
	 * and triggers it to update its upgrades
	 */
	private void updateConnectedUpgradeBlock(@NotNull World world, @NotNull BlockPos pos) {
		for (Vec3i possibleUpgradeBlockOffset : possibleUpgradeBlockOffsets) {
			BlockPos currentPos = pos.add(possibleUpgradeBlockOffset);
			BlockEntity blockEntity = world.getBlockEntity(currentPos);
			if (blockEntity instanceof Upgradeable upgradeable) {
				upgradeable.resetUpgrades();
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
		return new UpgradeBlockEntity(pos, state);
	}
	
}
