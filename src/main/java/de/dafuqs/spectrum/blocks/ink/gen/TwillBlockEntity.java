package de.dafuqs.spectrum.blocks.ink.gen;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;

public class TwillBlockEntity extends InkGeneratorBlockEntity {
	
	public static final int MAX_BLOCK_CHECK_DISTANCE_HORIZONTAL = 16;
	public static final int MAX_BLOCK_CHECK_DISTANCE_UP = 6;
	public static final long GENERATED_INK_PER_VALID_BLOCK = (long) Math.pow(2, 2);
	
	public TwillBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.TWILL.get(), blockPos, blockState, 1, 1);
	}
	
	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.spectrum.twill");
	}
	
	
	@Override
	public boolean tickLogic(Level level) {
		RandomSource random = level.getRandom();
		BlockPos posToTest = this.worldPosition.offset(random.nextIntBetweenInclusive(-MAX_BLOCK_CHECK_DISTANCE_HORIZONTAL, MAX_BLOCK_CHECK_DISTANCE_HORIZONTAL), random.nextInt(MAX_BLOCK_CHECK_DISTANCE_UP), random.nextIntBetweenInclusive(-MAX_BLOCK_CHECK_DISTANCE_HORIZONTAL, MAX_BLOCK_CHECK_DISTANCE_HORIZONTAL));
		BlockState stateToTest = level.getBlockState(posToTest);
		
		if(stateToTest.getBlock() instanceof ColoredLeavesBlock coloredLeavesBlock) {
			InkColor inkColor = coloredLeavesBlock.getColor();
			this.inkStorage.addEnergy(inkColor, GENERATED_INK_PER_VALID_BLOCK);
			
			if (SpectrumConfig.CONFIG.BlockSoundVolume.get() > 0) {
				level.playSound(null, worldPosition, SpectrumSoundEvents.COLOR_PICKER_PROCESSING, SoundSource.BLOCKS, SpectrumConfig.CONFIG.BlockSoundVolume.get().floatValue() / 3F, 1.0F);
			}
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) level,
					new Vec3(worldPosition.getX() + 0.5, worldPosition.getY() + 0.7, worldPosition.getZ() + 0.5),
					ColoredFluidRisingParticleEffect.of(inkColor.getColorInt()),
					5,
					new Vec3(0.22, 0.0, 0.22),
					new Vec3(0.0, 0.1, 0.0)
			);
			return true;
		}
		return false;
	}
	
}
