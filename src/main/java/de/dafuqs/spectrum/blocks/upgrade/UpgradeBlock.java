package de.dafuqs.spectrum.blocks.upgrade;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class UpgradeBlock extends BaseEntityBlock {
	
	protected static final VoxelShape SHAPE_UP = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
	private static final List<Block> upgradeBlocks = new ArrayList<>();
	
	private final Upgradeable.UpgradeType upgradeType;
	private final int upgradeMod;
	private final int effectColor;
	
	public UpgradeBlock(Properties settings, Upgradeable.UpgradeType upgradeType, int upgradeMod, int effectColor) {
		super(settings);
		this.upgradeType = upgradeType;
		this.upgradeMod = upgradeMod;
		this.effectColor = effectColor;

		upgradeBlocks.add(this);
	}

	@Override
	public MapCodec<? extends UpgradeBlock> codec() {
		//TODO: Make the codec
		return null;
	}

	public static List<Block> getUpgradeBlocks() {
		return upgradeBlocks;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE_UP;
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onPlace(state, world, pos, oldState, notify);
		if (!world.isClientSide) {
			updateConnectedUpgradeBlock((ServerLevel) world, pos);
		}
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		super.onRemove(state, world, pos, newState, moved);
		if (!world.isClientSide) {
			updateConnectedUpgradeBlock((ServerLevel) world, pos);
		}
	}

	/**
	 * When placed or removed the upgrade block searches for a valid Upgradeable block
	 * and triggers it to update its upgrades
	 */
	private void updateConnectedUpgradeBlock(@NotNull ServerLevel world, @NotNull BlockPos pos) {
		for (Vec3i possibleUpgradeBlockOffset : Upgradeable.POSSIBLE_UPGRADE_POS_OFFSETS) {
			BlockPos currentPos = pos.offset(possibleUpgradeBlockOffset);
			BlockEntity blockEntity = world.getBlockEntity(currentPos);
			if (blockEntity instanceof Upgradeable upgradeable && upgradeable.getUpgradePosOffsets().contains(possibleUpgradeBlockOffset)) {
				upgradeable.resetUpgrades();
				playConnectedParticles(world, pos, currentPos);
			}
		}
	}
	
	private void playConnectedParticles(@NotNull ServerLevel world, @NotNull BlockPos pos, BlockPos currentPos) {
		int particleColor = getEffectColor();
		world.playSound(null, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, SpectrumSoundEvents.CRAFTING_DING, SoundSource.BLOCKS, 1.0F, 1.0F);
		
		// TODO: port
		/*PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity(
				world, Vec3.atCenterOf(pos),
				ColoredSparkleRisingParticleEffect.of(particleColor),
				10, new Vec3(0.5, 0.5, 0.5),
				new Vec3(0.1, 0.1, 0.1));
		ColorTransmissionPayload.playColorTransmissionParticle(
				world,
				new ColoredTransmission(
						new Vec3(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D),
						new BlockPositionSource(currentPos), 6,
						particleColor)
		);*/
	}
	
	private int getEffectColor() {
		return this.effectColor;
	}

	public Upgradeable.UpgradeType getUpgradeType() {
		return this.upgradeType;
	}

	public int getUpgradeMod() {
		return this.upgradeMod;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new UpgradeBlockEntity(pos, state);
	}
	
}
