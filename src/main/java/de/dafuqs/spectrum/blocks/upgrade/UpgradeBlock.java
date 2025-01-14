package de.dafuqs.spectrum.blocks.upgrade;

import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class UpgradeBlock extends BlockWithEntity {

	protected static final VoxelShape SHAPE_UP = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
	private static final List<Block> upgradeBlocks = new ArrayList<>();
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
		
		// Cinderhearth
		add(new Vec3i(1, -1, 2));
		add(new Vec3i(-1, -1, 2));
		add(new Vec3i(1, -1, -2));
		add(new Vec3i(-1, -1, -2));
		add(new Vec3i(2, -1, 1));
		add(new Vec3i(-2, -1, 1));
		add(new Vec3i(2, -1, -1));
		add(new Vec3i(-2, -1, -1));
	}};
	// Like: The further the player progresses,
	// the higher are the chances for good mods?
	private final Upgradeable.UpgradeType upgradeType;
	private final int upgradeMod;
	private final DyeColor effectColor;

	public UpgradeBlock(Settings settings, Upgradeable.UpgradeType upgradeType, int upgradeMod, DyeColor effectColor) {
		super(settings);
		this.upgradeType = upgradeType;
		this.upgradeMod = upgradeMod;
		this.effectColor = effectColor;

		upgradeBlocks.add(this);
	}

	public static List<Block> getUpgradeBlocks() {
		return upgradeBlocks;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE_UP;
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (!world.isClient) {
			updateConnectedUpgradeBlock((ServerWorld) world, pos);
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		if (!world.isClient) {
			updateConnectedUpgradeBlock((ServerWorld) world, pos);
		}
	}

	/**
	 * When placed or removed the upgrade block searches for a valid Upgradeable block
	 * and triggers it to update its upgrades
	 */
	private void updateConnectedUpgradeBlock(@NotNull ServerWorld world, @NotNull BlockPos pos) {
		for (Vec3i possibleUpgradeBlockOffset : possibleUpgradeBlockOffsets) {
			BlockPos currentPos = pos.add(possibleUpgradeBlockOffset);
			BlockEntity blockEntity = world.getBlockEntity(currentPos);
			if (blockEntity instanceof Upgradeable upgradeable) {
				upgradeable.resetUpgrades();
				playConnectedParticles(world, pos, currentPos);
			}
		}
	}

	private void playConnectedParticles(@NotNull ServerWorld world, @NotNull BlockPos pos, BlockPos currentPos) {
		DyeColor particleColor = getEffectColor();
		world.playSound(null, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, SpectrumSoundEvents.CRAFTING_DING, SoundCategory.BLOCKS, 1.0F, 1.0F);
		SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(
				world, Vec3d.ofCenter(pos),
				SpectrumParticleTypes.getSparkleRisingParticle(particleColor),
				10, new Vec3d(0.5, 0.5, 0.5),
				new Vec3d(0.1, 0.1, 0.1));
		SpectrumS2CPacketSender.playColorTransmissionParticle(
				world,
				new ColoredTransmission(
						new Vec3d(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D),
						new BlockPositionSource(currentPos), 6,
						particleColor)
		);
	}

	private DyeColor getEffectColor() {
		return this.effectColor;
	}

	public Upgradeable.UpgradeType getUpgradeType() {
		return this.upgradeType;
	}

	public int getUpgradeMod() {
		return this.upgradeMod;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new UpgradeBlockEntity(pos, state);
	}
	
}
