package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.*;
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

public class FadingBlock extends DecayBlock {
	
	public FadingBlock(Settings settings) {
		super(settings, SpectrumCommon.CONFIG.FadingDecayTickRate, SpectrumCommon.CONFIG.FadingCanDestroyBlockEntities, 1, 1F);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		
		if (!world.isClient) {
			world.playSound(null, pos, SpectrumSoundEvents.FADING_PLACED, SoundCategory.BLOCKS, 0.5F, 1.0F);
		} else {
			Random random = world.getRandom();
			world.addParticle(ParticleTypes.POOF, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			
			for (int i = 0; i < 10; i++) {
				world.addParticle(SpectrumParticleTypes.DECAY_PLACE, pos.getX() + random.nextFloat(), pos.getY() + 1, pos.getZ() + random.nextFloat(), ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			}
		}
	}
	
	@Override
	protected @Nullable BlockState getSpreadState(BlockState stateToSpreadFrom, BlockState stateToSpreadTo) {
		if (stateToSpreadTo.isIn(SpectrumBlockTags.FADING_SPECIAL_CONVERSIONS)) {
			return stateToSpreadFrom.with(CONVERSION, Conversion.SPECIAL);
		} else if (stateToSpreadTo.isIn(SpectrumBlockTags.FADING_CONVERSIONS)) {
			return stateToSpreadFrom.with(CONVERSION, Conversion.DEFAULT);
		}
		return null;
	}
	
}
