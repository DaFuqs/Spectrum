package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class FailingBlock extends DecayBlock {
	
	public static final IntProperty AGE = Properties.AGE_15; // failing may spread 15 blocks max. It consuming obsidian resets that value
	
	public FailingBlock(Settings settings) {
		super(settings, null, SpectrumBlockTags.FAILING_SAFE, 2, 2.5F);
		
		setDefaultState(getStateManager().getDefaultState().with(CONVERSION, Conversion.NONE).with(AGE, 0));
		addDecayConversion(SpectrumBlockTags.FAILING_SPECIAL_CONVERSIONS, this.getDefaultState().with(CONVERSION, Conversion.SPECIAL));
		addDecayConversion(SpectrumBlockTags.FAILING_CONVERSIONS, this.getDefaultState().with(CONVERSION, Conversion.DEFAULT));
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		
		if (!world.isClient) {
			world.playSound(null, pos, SpectrumSoundEvents.FAILING_PLACED, SoundCategory.BLOCKS, 0.5F, 1.0F);
		} else {
			Random random = world.getRandom();
			world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			
			for (int i = 0; i < 20; i++) {
				world.addParticle(SpectrumParticleTypes.DECAY_PLACE, pos.getX() - 0.2 + random.nextFloat() * 1.4, pos.getY() + random.nextFloat(), pos.getZ() - 0.2 + random.nextFloat() * 1.4, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F), 0.05, ((-1.0F + random.nextFloat() * 2.0F) / 12.0F));
			}
		}
	}
	
	@Override
	protected float getSpreadChance() {
		return SpectrumCommon.CONFIG.FailingDecayTickRate;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(AGE);
	}
	
	@Override
	protected boolean canSpread(BlockState blockState) {
		return blockState.get(AGE) < Properties.AGE_15_MAX;
	}
	
	@Override
	protected boolean canSpreadToBlockEntities() {
		return SpectrumCommon.CONFIG.FailingCanDestroyBlockEntities;
	}
	
	@Override
	protected BlockState getSpreadState(BlockState previousState) {
		return previousState.with(AGE, previousState.get(AGE) + 1);
	}
	
}
