package de.dafuqs.spectrum.blocks.ink.gen;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.blocks.decay.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.events.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
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
	
	public VantaclastBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.VANTACLAST.get(), blockPos, blockState, 3);
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.vantaclast");
	}
	
	@Override
	protected boolean tickLogic(Level level) {
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
							new Vec3(posToTest.getX() + 0.5D, posToTest.getY() + 0.5D, posToTest.getZ() + 0.5D),
							new BlockPositionSource(worldPosition), 8,
							inkColor.getColorInt())
			);
			
			level.setBlockAndUpdate(posToTest, SpectrumBlocks.BLACK_MATERIA.get().defaultBlockState());
			
			return true;
		}
		return false;
	}
	
}
