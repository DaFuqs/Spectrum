package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class RuinBlock extends DecayBlock {
	
	public RuinBlock(Settings settings) {
		super(settings, SpectrumCommon.CONFIG.RuinDecayTickRate, SpectrumCommon.CONFIG.RuinCanDestroyBlockEntities, 3, 5F);
		setDefaultState(getStateManager().getDefaultState().with(CONVERSION, Conversion.NONE));
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		
		if (!world.isClient) {
			world.playSound(null, pos, SpectrumSoundEvents.RUIN_PLACED, SoundCategory.BLOCKS, 0.5F, 1.0F);
		} else {
			Random random = world.getRandom();
			world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			world.addParticle(ParticleTypes.EXPLOSION_EMITTER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			
			for (int i = 0; i < 40; i++) {
				world.addParticle(SpectrumParticleTypes.DECAY_PLACE, pos.getX() - 0.5 + random.nextFloat() * 2, pos.getY() + random.nextFloat(), pos.getZ() - 0.5 + random.nextFloat() * 2, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			}
		}
	}
	
	@Override
	protected @Nullable BlockState getSpreadState(BlockState stateToSpreadFrom, BlockState stateToSpreadTo, World world, BlockPos stateToSpreadToPos) {
		if (stateToSpreadTo.getCollisionShape(world, stateToSpreadToPos).isEmpty() || stateToSpreadTo.isIn(SpectrumBlockTags.RUIN_SAFE)) {
			return null;
		}
		
		if (stateToSpreadTo.isIn(SpectrumBlockTags.RUIN_SPECIAL_CONVERSIONS)) {
			return this.getDefaultState().with(CONVERSION, Conversion.SPECIAL);
		} else if (stateToSpreadTo.isIn(SpectrumBlockTags.RUIN_CONVERSIONS)) {
			// Protect the end portal to not lock players in the dim
			if (world.getRegistryKey().equals(World.END) && Math.abs(stateToSpreadToPos.getX()) < 8 && Math.abs(stateToSpreadToPos.getZ()) < 8) {
				return null;
			}
			
			return this.getDefaultState().with(CONVERSION, Conversion.DEFAULT);
		}
		return stateToSpreadFrom.with(CONVERSION, Conversion.NONE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		
		if (state.get(RuinBlock.CONVERSION) != Conversion.NONE && newState.isAir()) {
			if (world.getRegistryKey() == World.NETHER) {
				if (pos.getY() == world.getBottomY() + world.getDimension().logicalHeight() - 1) { // Attempt to match the nether ceiling. Tricky...
					world.setBlockState(pos, SpectrumBlocks.DEEPER_DOWN_PORTAL.getDefaultState().with(DeeperDownPortalBlock.FACING_UP, true), 3);
				} else if (pos.getY() == world.getBottomY()) {
					world.setBlockState(pos, SpectrumBlocks.DEEPER_DOWN_PORTAL.getDefaultState().with(DeeperDownPortalBlock.FACING_UP, false), 3);
				}
			} else if (world.getRegistryKey() == World.OVERWORLD && pos.getY() == world.getBottomY()) {
				world.setBlockState(pos, SpectrumBlocks.DEEPER_DOWN_PORTAL.getDefaultState().with(DeeperDownPortalBlock.FACING_UP, false), 3);
			} else if (world.getRegistryKey() == SpectrumDimensions.DIMENSION_KEY && pos.getY() == world.getTopY() - 1) { // highest layer cannot be built on
				world.setBlockState(pos, SpectrumBlocks.DEEPER_DOWN_PORTAL.getDefaultState().with(DeeperDownPortalBlock.FACING_UP, true), 3);
			}
		}
	}
	
}
