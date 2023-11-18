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
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class FailingBlock extends DecayBlock {
	
	public static final IntProperty AGE = Properties.AGE_15; // failing may spread 15 blocks max. It consuming obsidian resets that value
	
	public FailingBlock(Settings settings) {
		super(settings, SpectrumCommon.CONFIG.FailingDecayTickRate, SpectrumCommon.CONFIG.FailingCanDestroyBlockEntities, 2, 2.5F);
		setDefaultState(getStateManager().getDefaultState().with(CONVERSION, Conversion.NONE).with(AGE, 0));
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
	public boolean hasRandomTicks(BlockState state) {
		return state.get(AGE) < Properties.AGE_15_MAX;
	}
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(AGE);
	}
	
	@Override
	protected @Nullable BlockState getSpreadState(BlockState stateToSpreadFrom, BlockState stateToSpreadTo, World world, BlockPos stateToSpreadToPos) {
		if (stateToSpreadFrom.get(AGE) >= Properties.AGE_15_MAX) {
			return null;
		}
		if (stateToSpreadTo.getCollisionShape(world, stateToSpreadToPos).isEmpty() || stateToSpreadTo.isIn(SpectrumBlockTags.FAILING_SAFE)) {
			return null;
		}
		
		int age = stateToSpreadFrom.get(AGE);
		
		if (stateToSpreadTo.isIn(SpectrumBlockTags.FAILING_SPECIAL_CONVERSIONS)) {
			return this.getDefaultState().with(CONVERSION, Conversion.SPECIAL).with(AGE, Math.max(0, age - 5));
		} else if (stateToSpreadTo.isIn(SpectrumBlockTags.FAILING_CONVERSIONS)) {
			return this.getDefaultState().with(CONVERSION, Conversion.DEFAULT).with(AGE, Math.max(0, age - 2));
		}
		return stateToSpreadFrom.with(CONVERSION, Conversion.NONE).with(AGE, age + 1);
	}
	
}
