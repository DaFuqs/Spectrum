package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.piston.*;
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
		super(settings, SpectrumBlockTags.FADING_CONVERSIONS, null, 1, 1F);
		
		setDefaultState(getStateManager().getDefaultState().with(CONVERSION, Conversion.NONE));
		addDecayConversion(SpectrumBlockTags.FADING_SPECIAL_CONVERSIONS, this.getDefaultState().with(CONVERSION, Conversion.SPECIAL));
		addDecayConversion(SpectrumBlockTags.FADING_CONVERSIONS, this.getDefaultState().with(CONVERSION, Conversion.DEFAULT));
	}
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.NORMAL;
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
	protected float getSpreadChance() {
		return SpectrumCommon.CONFIG.FadingDecayTickRate;
	}
	
	@Override
	protected boolean canSpreadToBlockEntities() {
		return SpectrumCommon.CONFIG.FadingCanDestroyBlockEntities;
	}
	
	@Override
	protected boolean canSpread(BlockState blockState) {
		return true;
	}
	
	@Override
	protected BlockState getSpreadState(BlockState previousState) {
		return this.getDefaultState();
	}
	
}
