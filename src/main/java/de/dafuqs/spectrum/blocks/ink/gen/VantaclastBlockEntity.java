package de.dafuqs.spectrum.blocks.ink.gen;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.ink.storage.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.blocks.pedestal.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

public class VantaclastBlockEntity extends InkGeneratorBlockEntity {
	
	public static final int MAX_BLOCK_CHECK_DISTANCE_HORIZONTAL = 16;
	public static final int MAX_BLOCK_CHECK_DISTANCE_UP = 6;
	public static final long GENERATED_INK_PER_VALID_BLOCK = (long) Math.pow(2, 16);
	
	private long lastConvertedBlockTick = -1L;
	
	public VantaclastBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.VANTACLAST.get(), blockPos, blockState, 3, 1);
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.vantaclast");
	}
	
	@Override
	public boolean tickLogic(Level level) {
		RandomSource random = level.getRandom();
		BlockPos posToTest = this.worldPosition.offset(random.nextIntBetweenInclusive(-MAX_BLOCK_CHECK_DISTANCE_HORIZONTAL, MAX_BLOCK_CHECK_DISTANCE_HORIZONTAL), random.nextInt(MAX_BLOCK_CHECK_DISTANCE_UP), random.nextIntBetweenInclusive(-MAX_BLOCK_CHECK_DISTANCE_HORIZONTAL, MAX_BLOCK_CHECK_DISTANCE_HORIZONTAL));
		BlockState statetoTest = level.getBlockState(posToTest);
		
		if(statetoTest.getBlock() instanceof ColoredLeavesBlock coloredLeavesBlock) {
			InkColor inkColor = coloredLeavesBlock.getColor();
			this.inkStorage.addEnergy(inkColor, GENERATED_INK_PER_VALID_BLOCK);
			
			if (SpectrumConfig.CONFIG.BlockSoundVolume.get() > 0) {
				level.playSound(null, worldPosition, SpectrumSoundEvents.COLOR_PICKER_PROCESSING, SoundSource.BLOCKS, SpectrumConfig.CONFIG.BlockSoundVolume.get().floatValue() / 3F, 1.0F);
			}
			
			ColorTransmissionPayload.playColorTransmissionParticle(
					(ServerLevel) level,
					new ColoredTransmission(
							Vec3.atCenterOf(posToTest),
							new BlockPositionSource(worldPosition), 8,
							inkColor.getColorARGB())
			);
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) level,
					new Vec3(worldPosition.getX() + 0.5, worldPosition.getY() + 0.7, worldPosition.getZ() + 0.5),
					ColoredFluidRisingParticleEffect.of(inkColor.getColorARGB()),
					5,
					new Vec3(0.22, 0.0, 0.22),
					new Vec3(0.0, 0.1, 0.0)
			);
			
			level.setBlockAndUpdate(posToTest, SpectrumBlocks.BLACK_MATERIA.get().defaultBlockState());
			
			lastConvertedBlockTick = level.getGameTime();
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean pauseWhenInactive() {
		return false;
	}
	
	@Override
	protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		nbt.putLong("last_converted_block_tick", lastConvertedBlockTick);
	}
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		this.lastConvertedBlockTick = nbt.getLong("last_converted_block_tick");
	}
	
	public long getLastConvertedBlockTick() {
		return lastConvertedBlockTick;
	}
	
}
