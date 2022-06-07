package de.dafuqs.spectrum.blocks.gemstone;

import de.dafuqs.spectrum.events.SpectrumGameEvents;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.minecraft.block.AmethystClusterBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class SpectrumBuddingBlock extends BuddingAmethystBlock {
	
	private static final Direction[] DIRECTIONS = Direction.values();
	private final Block smallBlock;
	private final Block mediumBlock;
	private final Block largeBlock;
	private final Block clusterBlock;
	private final SoundEvent hitSoundEvent;
	private final SoundEvent chimeSoundEvent;
	
	public SpectrumBuddingBlock(Settings settings, Block smallBlock, Block mediumBlock, Block largeBlock, Block clusterBlock, SoundEvent hitSoundEvent, SoundEvent chimeSoundEvent) {
		super(settings);
		
		this.smallBlock = smallBlock;
		this.mediumBlock = mediumBlock;
		this.largeBlock = largeBlock;
		this.clusterBlock = clusterBlock;
		
		this.hitSoundEvent = hitSoundEvent;
		this.chimeSoundEvent = chimeSoundEvent;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextInt(5) == 0) {
			Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
			BlockPos blockPos = pos.offset(direction);
			BlockState blockState = world.getBlockState(blockPos);
			Block block = null;
			if (BuddingAmethystBlock.canGrowIn(blockState)) {
				block = smallBlock;
			} else if (blockState.isOf(smallBlock) && blockState.get(AmethystClusterBlock.FACING) == direction) {
				block = mediumBlock;
			} else if (blockState.isOf(mediumBlock) && blockState.get(AmethystClusterBlock.FACING) == direction) {
				block = largeBlock;
			} else if (blockState.isOf(largeBlock) && blockState.get(AmethystClusterBlock.FACING) == direction) {
				block = clusterBlock;
			}
			
			if (block != null) {
				BlockState blockState2 = (block.getDefaultState().with(AmethystClusterBlock.FACING, direction)).with(AmethystClusterBlock.WATERLOGGED, blockState.getFluidState().getFluid() == Fluids.WATER);
				world.setBlockState(blockPos, blockState2);
				if (blockState2.isIn(SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE)) {
					world.emitGameEvent(SpectrumGameEvents.CRYSTAL_APOTHECARY_HARVESTABLE_GROWN, blockPos);
				}
			}
		}
	}
	
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		if (!world.isClient) {
			BlockPos blockPos = hit.getBlockPos();
			world.playSound(null, blockPos, hitSoundEvent, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
			world.playSound(null, blockPos, chimeSoundEvent, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
		}
	}
	
}
